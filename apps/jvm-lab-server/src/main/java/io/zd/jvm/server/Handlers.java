package io.zd.jvm.server;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public final class Handlers {

  private static final ConcurrentHashMap<String, ReentrantLock> LOCKS = new ConcurrentHashMap<>();

  private Handlers() {}

  public static void health(HttpExchange ex) throws IOException {
    respond(ex, 200, "OK\n");
  }

  public static void cpu(HttpExchange ex) throws IOException {
    long start = System.nanoTime();
    Map<String, String> q = Query.parse(ex.getRequestURI());
    int ms = Query.intParam(q, "ms", 200);

    long until = System.nanoTime() + ms * 1_000_000L;
    long x = 0;
    while (System.nanoTime() < until) {
      x = x * 31 + 7;
    }
    respond(ex, 200, "cpu_done x=" + x + "\n");
    Metrics.record("/cpu", System.nanoTime() - start);
  }

  public static void alloc(HttpExchange ex) throws IOException {
    long start = System.nanoTime();
    Map<String, String> q = Query.parse(ex.getRequestURI());
    int mb = Query.intParam(q, "mb", 10);
    int chunks = Query.intParam(q, "chunks", 10); // pour varier patterns d'alloc
    int bytes = mb * 1024 * 1024;
    int per = Math.max(1, bytes / chunks);

    byte[][] arr = new byte[chunks][];
    for (int i = 0; i < chunks; i++) {
      arr[i] = new byte[per];
      arr[i][0] = (byte) i;
    }
    // empêcher l'optimisation triviale
    int checksum = 0;
    for (byte[] a : arr) checksum += a[0];

    respond(ex, 200, "alloc_done mb=" + mb + " checksum=" + checksum + "\n");
    Metrics.record("/alloc", System.nanoTime() - start);
  }

  public static void sleep(HttpExchange ex) throws IOException {
    long start = System.nanoTime();
    Map<String, String> q = Query.parse(ex.getRequestURI());
    int ms = Query.intParam(q, "ms", 200);
    try {
      Thread.sleep(ms);
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
      respond(ex, 500, "interrupted\n");
      Metrics.record("/sleep", System.nanoTime() - start);
      return;
    }
    respond(ex, 200, "sleep_done ms=" + ms + "\n");
    Metrics.record("/sleep", System.nanoTime() - start);
  }

  public static void lock(HttpExchange ex) throws IOException {
    long start = System.nanoTime();
    Map<String, String> q = Query.parse(ex.getRequestURI());
    int ms = Query.intParam(q, "ms", 200);
    String key = Query.strParam(q, "key", "global");
    ReentrantLock lock = LOCKS.computeIfAbsent(key, k -> new ReentrantLock());
    lock.lock();
    try {
      try {
        Thread.sleep(ms);
      } catch (InterruptedException ie) {
        Thread.currentThread().interrupt();
      }
    } finally {
      lock.unlock();
    }
    respond(ex, 200, "lock_done key=" + key + " ms=" + ms + "\n");
    Metrics.record("/lock", System.nanoTime() - start);
  }

  public static void leak(HttpExchange ex) throws IOException {
    long start = System.nanoTime();
    Map<String, String> q = Query.parse(ex.getRequestURI());
    int mb = Query.intParam(q, "mb", 50);
    String action = Query.strParam(q, "action", "add"); // add|clear|count

    String body;
    switch (action) {
      case "add" -> {
        long n = LeakStore.addMegabytes(mb);
        body = "leak_added mb=" + mb + " blocks=" + n + "\n";
      }
      case "clear" -> {
        LeakStore.clear();
        body = "leak_cleared\n";
      }
      case "count" -> body = "leak_blocks=" + LeakStore.count() + "\n";
      default -> body = "unknown action (use add|clear|count)\n";
    }
    respond(ex, 200, body);
    Metrics.record("/leak", System.nanoTime() - start);
  }

  public static void direct(HttpExchange ex) throws IOException {
    long start = System.nanoTime();
    Map<String, String> q = Query.parse(ex.getRequestURI());
    int mb = Query.intParam(q, "mb", 50);

    int bytes = mb * 1024 * 1024;
    ByteBuffer buf = ByteBuffer.allocateDirect(bytes);
    buf.putInt(0, 42);

    respond(ex, 200, "direct_alloc_done mb=" + mb + " v=" + buf.getInt(0) + "\n");
    Metrics.record("/direct", System.nanoTime() - start);
  }

  public static void metrics(HttpExchange ex) throws IOException {
    long start = System.nanoTime();
    respond(ex, 200, Metrics.render());
    Metrics.record("/metrics", System.nanoTime() - start);
  }

  private static void respond(HttpExchange ex, int status, String body) throws IOException {
    byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
    ex.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
    ex.getResponseHeaders().set("X-Server-Time", Instant.now().toString());
    ex.sendResponseHeaders(status, bytes.length);
    try (OutputStream os = ex.getResponseBody()) {
      os.write(bytes);
    }
  }
}
