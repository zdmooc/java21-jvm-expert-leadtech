package io.zd.jvm.loadgen;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public final class LoadRunner {
  private final URI uri;
  private final int concurrency;
  private final Duration duration;
  private final String mode;

  public LoadRunner(URI uri, int concurrency, Duration duration, String mode) {
    this.uri = uri;
    this.concurrency = concurrency;
    this.duration = duration;
    this.mode = mode == null ? "virtual" : mode;
  }

  public LoadResult run() throws Exception {
    HttpClient client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(2))
        .version(HttpClient.Version.HTTP_1_1)
        .build();

    ExecutorService executor = switch (mode) {
      case "virtual" -> Executors.newVirtualThreadPerTaskExecutor();
      case "fixed" -> Executors.newFixedThreadPool(Math.min(concurrency, 500));
      default -> throw new IllegalArgumentException("mode must be virtual|fixed");
    };

    LatencyHistogram hist = new LatencyHistogram();

    long endAt = System.nanoTime() + duration.toNanos();
    List<java.util.concurrent.Future<?>> tasks = new ArrayList<>(concurrency);

    for (int i = 0; i < concurrency; i++) {
      tasks.add(executor.submit(() -> {
        while (System.nanoTime() < endAt) {
          long t0 = System.nanoTime();
          boolean ok = oneRequest(client);
          long t1 = System.nanoTime();
          hist.record((t1 - t0) / 1_000_000.0, ok);
          // jitter léger pour éviter patterns trop réguliers
          try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(0, 3));
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
          }
        }
      }));
    }

    for (var f : tasks) f.get();
    executor.close();
    return hist.toResult(duration);
  }

  private boolean oneRequest(HttpClient client) {
    try {
      HttpRequest req = HttpRequest.newBuilder()
          .uri(uri)
          .timeout(Duration.ofSeconds(10))
          .GET()
          .build();
      HttpResponse<Void> resp = client.send(req, HttpResponse.BodyHandlers.discarding());
      return resp.statusCode() >= 200 && resp.statusCode() < 300;
    } catch (IOException | InterruptedException e) {
      if (e instanceof InterruptedException) Thread.currentThread().interrupt();
      return false;
    }
  }
}
