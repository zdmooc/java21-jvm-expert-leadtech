# Runbook — Incident OOM (Java 21)

## Symptômes
- crash JVM `OutOfMemoryError`
- pod killed (OOMKilled) en K8s
- hausse latence + GC pauses + mémoire qui monte

## Triage (10 minutes)
1) Confirmer type d’OOM
- heap: `java.lang.OutOfMemoryError: Java heap space`
- metaspace: `OutOfMemoryError: Metaspace`
- native/direct: `OutOfMemoryError: Direct buffer memory` / `unable to create new native thread`

2) Capturer evidences
- GC logs (`logs/gc.log`)
- heap dump (si heap OOM): `scripts/take-heap-dump.sh`
- thread dump: `scripts/take-thread-dump.sh`
- (si possible) JFR 60s

## Analyse
- Heap dump (MAT): dominators, retained size, suspects
- GC logs: allocation rate, promotion, full GCs
- Direct memory: vérifier NIO direct buffers, Netty, etc.

## Actions correctives
- Court terme: augmenter limite mémoire OU réduire charge + rollback
- Moyen terme: corriger leak (cache non borné, collections statiques, listeners, etc.)
- Long terme: instrumentation + alerting (allocation rate, heap usage slope)

## Validation
- répéter test charge
- vérifier p99 et stabilité heap
