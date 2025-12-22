package io.zd.jvm.loadgen;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;

public final class LatencyHistogram {
  private final List<Double> samples = Collections.synchronizedList(new ArrayList<>(200_000));
  private final LongAdder ok = new LongAdder();
  private final LongAdder ko = new LongAdder();
  private final LongAdder sumMs = new LongAdder(); // approximation (ms arrondis)

  public void record(double ms, boolean success) {
    samples.add(ms);
    if (success) ok.increment(); else ko.increment();
    sumMs.add((long) (ms * 1000)); // micro-ms for avg precision
  }

  public LoadResult toResult(Duration duration) {
    List<Double> copy;
    synchronized (samples) {
      copy = new ArrayList<>(samples);
    }
    copy.sort(Double::compareTo);

    long total = ok.sum() + ko.sum();
    double avg = total == 0 ? 0.0 : (sumMs.sum() / 1000.0) / total;

    return new LoadResult(
        total,
        ko.sum(),
        total / (double) duration.toSeconds(),
        avg,
        percentile(copy, 50),
        percentile(copy, 95),
        percentile(copy, 99)
    );
  }

  private static double percentile(List<Double> sorted, int p) {
    if (sorted.isEmpty()) return 0.0;
    double rank = (p / 100.0) * (sorted.size() - 1);
    int lo = (int) Math.floor(rank);
    int hi = (int) Math.ceil(rank);
    if (lo == hi) return sorted.get(lo);
    double w = rank - lo;
    return sorted.get(lo) * (1 - w) + sorted.get(hi) * w;
  }
}
