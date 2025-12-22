package io.zd.jvm.jmh;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(2)
@State(Scope.Thread)
public class StringConcatBench {

  @Param({"10", "100"})
  int n;

  @Benchmark
  public String plusConcat() {
    String s = "";
    for (int i = 0; i < n; i++) s += i;
    return s;
  }

  @Benchmark
  public String builderConcat() {
    StringBuilder sb = new StringBuilder(n * 3);
    for (int i = 0; i < n; i++) sb.append(i);
    return sb.toString();
  }
}
