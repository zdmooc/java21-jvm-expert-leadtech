package io.zd.jvm.server;

import java.util.LongSummaryStatistics;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LongAdder;

/**
 * Metrics volontairement simples:
 * - compteur par endpoint
 * - stats latence en microsecondes (min/max/avg) par endpoint
 *
 * Objectif: donner un support minimal pour les labs JFR/profiling/troubleshooting.
 */
public final class Metrics {
  private static final ConcurrentHashMap<String, LongAdder> COUNTS = new ConcurrentHashMap<>();
  private static final ConcurrentHashMap<String, LongAdder> SUM_US = new ConcurrentHashMap<>();
  private static final ConcurrentHashMap<String, LongAdder> MIN_US = new ConcurrentHashMap<>();
  private static final ConcurrentHashMap<String, LongAdder> MAX_US = new ConcurrentHashMap<>();

  private Metrics() {}

  public static void record(String path, long durationNanos) {
    long us = durationNanos / 1_000L;
    COUNTS.computeIfAbsent(path, k -> new LongAdder()).increment();
    SUM_US.computeIfAbsent(path, k -> new LongAdder()).add(us);
    MIN_US.computeIfAbsent(path, k -> new LongAdder()).accumulate(Math.min(getMin(path), us));
    MAX_US.computeIfAbsent(path, k -> new LongAdder()).accumulate(Math.max(getMax(path), us));
  }

  private static long getMin(String path) {
    LongAdder a = MIN_US.get(path);
    return a == null ? Long.MAX_VALUE : a.sum();
  }
  private static long getMax(String path) {
    LongAdder a = MAX_US.get(path);
    return a == null ? 0L : a.sum();
  }

  public static String render() {
    StringBuilder sb = new StringBuilder(2048);
    sb.append("# simple_metrics\n");
    sb.append("# path count avg_us min_us max_us\n");
    COUNTS.forEach((path, count) -> {
      long c = count.sum();
      long sum = SUM_US.getOrDefault(path, new LongAdder()).sum();
      long min = MIN_US.getOrDefault(path, new LongAdder()).sum();
      long max = MAX_US.getOrDefault(path, new LongAdder()).sum();
      long avg = c == 0 ? 0 : (sum / c);
      if (min == 0 || min == Long.MAX_VALUE) min = 0;
      sb.append(path).append(' ')
        .append(c).append(' ')
        .append(avg).append(' ')
        .append(min).append(' ')
        .append(max).append('\n');
    });
    return sb.toString();
  }
}
