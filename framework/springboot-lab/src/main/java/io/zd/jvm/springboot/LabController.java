package io.zd.jvm.springboot;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReentrantLock;

@RestController
public class LabController {

  private static final ConcurrentHashMap<String, ReentrantLock> LOCKS = new ConcurrentHashMap<>();
  private static final ConcurrentHashMap<String, LongAdder> COUNT = new ConcurrentHashMap<>();
  private static final ConcurrentHashMap<String, LongAdder> TOTAL_NS = new ConcurrentHashMap<>();

  // fuite volontaire : on garde des tableaux dans une map (clé = instant) pour ne jamais les libérer
  private static final Map<String, byte[]> LEAKS = new ConcurrentHashMap<>();

  @GetMapping(value = "/health", produces = MediaType.TEXT_PLAIN_VALUE)
  public String health() {
    record("/health", 0);
    return "OK " + Instant.now() + " pid=" + pid() + "\n";
  }

  @GetMapping(value = "/cpu", produces = MediaType.TEXT_PLAIN_VALUE)
  public String cpu(@RequestParam(defaultValue = "200") int ms) {
    long start = System.nanoTime();
    long end = System.nanoTime() + ms * 1_000_000L;

    long x = 0;
    while (System.nanoTime() < end) {
      // un petit mélange pour éviter les optimisations trop agressives
      x = x * 1664525L + 1013904223L;
      x ^= (x >>> 13);
      x ^= (x << 7);
      x ^= (x >>> 17);
    }

    long dur = System.nanoTime() - start;
    record("/cpu", dur);
    return "cpu_done ms=" + ms + " x=" + x + " dur_ms=" + (dur / 1_000_000L) + "\n";
  }

  @GetMapping(value = "/alloc", produces = MediaType.TEXT_PLAIN_VALUE)
  public String alloc(@RequestParam(defaultValue = "10") int mb) {
    long start = System.nanoTime();
    int bytes = mb * 1024 * 1024;
    int per = 256 * 1024;
    int n = Math.max(1, bytes / per);

    byte[][] arr = new byte[n][];
    for (int i = 0; i < n; i++) {
      arr[i] = new byte[per];
      arr[i][0] = (byte) i;
    }
    int checksum = 0;
    for (byte[] a : arr) checksum += a[0];

    long dur = System.nanoTime() - start;
    record("/alloc", dur);
    return "alloc_done mb=" + mb + " checksum=" + checksum + " dur_ms=" + (dur / 1_000_000L) + "\n";
  }

  @GetMapping(value = "/sleep", produces = MediaType.TEXT_PLAIN_VALUE)
  public String sleep(@RequestParam(defaultValue = "200") int ms) throws InterruptedException {
    long start = System.nanoTime();
    Thread.sleep(ms);
    long dur = System.nanoTime() - start;
    record("/sleep", dur);
    return "sleep_done ms=" + ms + " dur_ms=" + (dur / 1_000_000L) + "\n";
  }

  @GetMapping(value = "/lock", produces = MediaType.TEXT_PLAIN_VALUE)
  public String lock(@RequestParam(defaultValue = "global") String key,
                     @RequestParam(defaultValue = "200") int holdMs) throws InterruptedException {
    long start = System.nanoTime();
    ReentrantLock lock = LOCKS.computeIfAbsent(key, k -> new ReentrantLock());
    lock.lock();
    try {
      Thread.sleep(holdMs);
      long dur = System.nanoTime() - start;
      record("/lock", dur);
      return "lock_done key=" + key + " holdMs=" + holdMs + " dur_ms=" + (dur / 1_000_000L) + "\n";
    } finally {
      lock.unlock();
    }
  }

  @GetMapping(value = "/leak", produces = MediaType.TEXT_PLAIN_VALUE)
  public String leak(@RequestParam(defaultValue = "50") int mb) {
    long start = System.nanoTime();
    int bytes = mb * 1024 * 1024;
    byte[] b = new byte[bytes];
    b[0] = 1;
    LEAKS.put(Instant.now().toString(), b);

    long dur = System.nanoTime() - start;
    record("/leak", dur);
    return "leak_done mb=" + mb + " leaks=" + LEAKS.size() + " dur_ms=" + (dur / 1_000_000L) + "\n";
  }

  @GetMapping(value = "/direct", produces = MediaType.TEXT_PLAIN_VALUE)
  public String direct(@RequestParam(defaultValue = "50") int mb) {
    long start = System.nanoTime();
    int bytes = mb * 1024 * 1024;
    ByteBuffer bb = ByteBuffer.allocateDirect(bytes);
    bb.putInt(0, 0xCAFE_BABE);
    long dur = System.nanoTime() - start;
    record("/direct", dur);
    return "direct_done mb=" + mb + " magic=" + Integer.toHexString(bb.getInt(0)) + " dur_ms=" + (dur / 1_000_000L) + "\n";
  }

  @GetMapping(value = "/metrics", produces = MediaType.TEXT_PLAIN_VALUE)
  public String metrics() {
    StringBuilder sb = new StringBuilder();
    sb.append("now=").append(Instant.now()).append("\n");
    sb.append("pid=").append(pid()).append("\n");
    for (String k : COUNT.keySet()) {
      long c = COUNT.getOrDefault(k, new LongAdder()).sum();
      long ns = TOTAL_NS.getOrDefault(k, new LongAdder()).sum();
      long avgNs = c == 0 ? 0 : (ns / c);
      sb.append(k).append(" count=").append(c)
        .append(" avg_ms=").append(avgNs / 1_000_000L)
        .append("\n");
    }
    return sb.toString();
  }

  private static void record(String path, long durNs) {
    COUNT.computeIfAbsent(path, k -> new LongAdder()).increment();
    TOTAL_NS.computeIfAbsent(path, k -> new LongAdder()).add(durNs);
  }

  private static String pid() {
    // "pid@hostname"
    String name = ManagementFactory.getRuntimeMXBean().getName();
    int idx = name.indexOf('@');
    return idx > 0 ? name.substring(0, idx) : name;
  }
}
