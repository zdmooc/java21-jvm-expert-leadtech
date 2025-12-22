# Runbook — Latence p95/p99 (Java 21)

## Symptômes
- p99 explose sans hausse CPU apparente
- timeouts clients
- saturation threads/pools

## Hypothèses fréquentes
- contention locks (monitor, ReentrantLock)
- I/O lente (DB, HTTP) + threads bloqués
- GC pauses
- CPU throttling (cgroups)
- allocation storm

## Triage rapide
1) Thread dump (2-3 fois à 5s d’intervalle)
```bash
./scripts/take-thread-dump.sh
```
Chercher: `BLOCKED`, `WAITING`, deadlock, pools saturés.

2) GC logs: spikes pauses / full GC
3) JFR 60s:
```bash
./scripts/start-jfr.sh latency 60s
```

## Interprétation
- Si beaucoup `BLOCKED`: runbook locks
- Si `TIMED_WAITING` sur I/O: vérifier dépendances aval
- Si GC: réduire alloc / tuning minimal / augmenter heap
- Si cgroups: vérifier throttling et limits

## Actions
- prioriser “réduire contention / réduire alloc / réduire IO” avant “augmenter machines”
