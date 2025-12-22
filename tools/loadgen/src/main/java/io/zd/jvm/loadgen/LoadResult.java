package io.zd.jvm.loadgen;

public record LoadResult(
    long total,
    long errors,
    double rps,
    double avgMs,
    double p50Ms,
    double p95Ms,
    double p99Ms
) {}
