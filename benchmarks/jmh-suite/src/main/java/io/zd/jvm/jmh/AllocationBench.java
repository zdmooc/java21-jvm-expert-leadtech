package io.zd.jvm.jmh;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(2)
@State(Scope.Thread)
public class AllocationBench {

  @Param({"256", "4096"})
  int bytes;

  @Benchmark
  public byte[] allocate() {
    return new byte[bytes];
  }
}
