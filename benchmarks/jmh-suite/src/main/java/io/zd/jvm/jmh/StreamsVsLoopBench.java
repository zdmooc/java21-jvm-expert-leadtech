package io.zd.jvm.jmh;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(2)
@State(Scope.Thread)
public class StreamsVsLoopBench {

  int[] data;

  @Setup(Level.Trial)
  public void setup() {
    data = new int[10_000];
    for (int i = 0; i < data.length; i++) data[i] = ThreadLocalRandom.current().nextInt(0, 1000);
  }

  @Benchmark
  public int loopSum() {
    int s = 0;
    for (int v : data) s += v;
    return s;
  }

  @Benchmark
  public int streamSum() {
    return IntStream.of(data).sum();
  }
}
