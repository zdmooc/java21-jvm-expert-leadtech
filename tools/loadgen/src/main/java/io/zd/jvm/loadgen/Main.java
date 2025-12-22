package io.zd.jvm.loadgen;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;

public final class Main {

  /**
   * Usage:
   *   java -jar loadgen.jar <url> <concurrency> <durationSeconds> [mode]
   *
   * mode:
   *   virtual  (default) - un virtual thread par tâche
   *   fixed              - pool fixe platform threads
   */
  public static void main(String[] args) throws Exception {
    if (args.length < 3) {
      System.err.println("Usage: loadgen <url> <concurrency> <durationSeconds> [mode=virtual|fixed]");
      System.exit(2);
    }
    URI uri = URI.create(args[0]);
    int conc = Integer.parseInt(args[1]);
    int seconds = Integer.parseInt(args[2]);
    String mode = args.length >= 4 ? args[3] : "virtual";

    LoadRunner runner = new LoadRunner(uri, conc, Duration.ofSeconds(seconds), mode);
    Instant start = Instant.now();
    LoadResult r = runner.run();
    Instant end = Instant.now();

    System.out.println("=== Loadgen Result ===");
    System.out.println("url       : " + uri);
    System.out.println("mode      : " + mode);
    System.out.println("concurrency: " + conc);
    System.out.println("duration  : " + Duration.between(start, end));
    System.out.println("requests  : " + r.total());
    System.out.println("errors    : " + r.errors());
    System.out.println("rps       : " + String.format("%.2f", r.rps()));
    System.out.println("avg_ms    : " + String.format("%.2f", r.avgMs()));
    System.out.println("p50_ms    : " + String.format("%.2f", r.p50Ms()));
    System.out.println("p95_ms    : " + String.format("%.2f", r.p95Ms()));
    System.out.println("p99_ms    : " + String.format("%.2f", r.p99Ms()));
  }
}
