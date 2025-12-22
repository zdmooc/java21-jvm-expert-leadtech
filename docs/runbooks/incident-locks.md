# Runbook — Contention / Deadlocks

## Signaux
- threads `BLOCKED` / `WAITING`
- RPS chute, latence monte
- CPU peut rester faible

## Triage
- 3 thread dumps (t0, t+5s, t+10s)
- JFR (lock profiling) si possible

## Lecture thread dump
- `Found one Java-level deadlock` => deadlock confirmé
- `BLOCKED (on object monitor)` => contention monitor
- `parking to wait for <...>` => locks/park

## Actions
- court terme: limiter concurrence, breaker, restart si deadlock
- moyen terme: réduire section critique, partitionner locks, éviter synchronized global
- long terme: tests de charge + profil lock, métriques contention
