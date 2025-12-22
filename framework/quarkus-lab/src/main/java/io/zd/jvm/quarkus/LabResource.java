package io.zd.jvm.quarkus;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.lang.management.ManagementFactory;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReentrantLock;

@Path("/")
public class LabResource {

  private static final ConcurrentHashMap<String, ReentrantLock> LOCKS = new ConcurrentHashMap<>();
  private static final ConcurrentHashMap<String, LongAdder> COUNT = new ConcurrentHashMap<>();
  private static final ConcurrentHashMap<String, LongAdder> TOTAL_NS = new ConcurrentHashMap<>();
  private static final Map<String, byte[]> LEAKS = new ConcurrentHashMap<>();

  @GET
  @Path("health")
  @Produces(MediaType.TEXT_PLAIN)
  public String health() {
    record("/health", 0);
    return "OK " + Instant.now() + " pid=" + pid() + "\n";
  }

  @GET
  @Path("cpu")
  @Produces(MediaType.TEXT_PLAIN)
  public String cpu(@QueryParam("ms") Integer msParam) {
    int ms = msParam == null ? 200 : msParam;
    long start = System.nanoTime();
    long end = System.nanoTime() + ms * 1_000_000L;

    long x = 0;
    while (System.nanoTime() < end) {
      x = x * 1664525L + 1013904223L;
      x ^= (x >>> 13);
      x ^= (x << 7);
      x ^= (x >>> 17);
    }

    long dur = System.nanoTime() - start;
    record("/cpu", dur);
    return "cpu_done ms=" + ms + " x=" + x + " dur_ms=" + (dur / 1_000_000L) + "\n";
  }

  @GET
  @Path("alloc")
  @Produces(MediaType.TEXT_PLAIN)
  public String alloc(@QueryParam("mb") Integer mbParam) {
    int mb = mbParam == null ? 10 : mbParam;
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

  @GET
  @Path("sleep")
  @Produces(MediaType.TEXT_PLAIN)
  @RunOnVirtualThread
  public String sleep(@QueryParam("ms") Integer msParam) throws InterruptedException {
    int ms = msParam == null ? 200 : msParam;
    long start = System.nanoTime();
    Thread.sleep(ms);
    long dur = System.nanoTime() - start;
    record("/sleep", dur);
    return "sleep_done ms=" + ms + " dur_ms=" + (dur / 1_000_000L) + "\n";
  }

  @GET
  @Path("lock")
  @Produces(MediaType.TEXT_PLAIN)
  @RunOnVirtualThread
  public String lock(@QueryParam("key") String keyParam,
                     @QueryParam("holdMs") Integer holdMsParam) throws InterruptedException {
    String key = (keyParam == null || keyParam.isBlank()) ? "global" : keyParam;
    int holdMs = holdMsParam == null ? 200 : holdMsParam;

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

  @GET
  @Path("leak")
  @Produces(MediaType.TEXT_PLAIN)
  public String leak(@QueryParam("mb") Integer mbParam) {
    int mb = mbParam == null ? 50 : mbParam;
    long start = System.nanoTime();
    int bytes = mb * 1024 * 1024;
    byte[] b = new byte[bytes];
    b[0] = 1;
    LEAKS.put(Instant.now().toString(), b);

    long dur = System.nanoTime() - start;
    record("/leak", dur);
    return "leak_done mb=" + mb + " leaks=" + LEAKS.size() + " dur_ms=" + (dur / 1_000_000L) + "\n";
  }

  @GET
  @Path("direct")
  @Produces(MediaType.TEXT_PLAIN)
  public String direct(@QueryParam("mb") Integer mbParam) {
    int mb = mbParam == null ? 50 : mbParam;
    long start = System.nanoTime();
    int bytes = mb * 1024 * 1024;
    ByteBuffer bb = ByteBuffer.allocateDirect(bytes);
    bb.putInt(0, 0xCAFE_BABE);

    long dur = System.nanoTime() - start;
    record("/direct", dur);
    return "direct_done mb=" + mb + " magic=" + Integer.toHexString(bb.getInt(0)) + " dur_ms=" + (dur / 1_000_000L) + "\n";
  }

  @GET
  @Path("metrics")
  @Produces(MediaType.TEXT_PLAIN)
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
    String name = ManagementFactory.getRuntimeMXBean().getName();
    int idx = name.indexOf('@');
    return idx > 0 ? name.substring(0, idx) : name;
  }
}
