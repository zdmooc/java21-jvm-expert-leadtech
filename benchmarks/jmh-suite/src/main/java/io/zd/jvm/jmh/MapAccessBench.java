package io.zd.jvm.jmh;

import org.openjdk.jmh.annotations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(2)
@State(Scope.Thread)
public class MapAccessBench {

  Map<Integer, Integer> map;
  int[] keys;

  @Setup(Level.Trial)
  public void setup() {
    map = new HashMap<>(100_000);
    keys = new int[100_000];
    for (int i = 0; i < keys.length; i++) {
      keys[i] = i;
      map.put(i, i);
    }
  }

  @Benchmark
  public int getHotKeys() {
    int sum = 0;
    for (int i = 0; i < 10_000; i++) {
      int k = ThreadLocalRandom.current().nextInt(0, 10_000);
      sum += map.get(k);
    }
    return sum;
  }
}
