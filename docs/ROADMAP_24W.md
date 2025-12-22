# Roadmap 24 semaines — consolidation expertise JVM

Après les 12 premières semaines, tu passes en mode “expert” :
- instrumentation (Java agents)
- optimisation avancée (JIT, allocation, locks)
- production-grade runbooks (K8s/OpenShift)
- études de cas (incidents simulés)

## Semaines 13-16 — Agents & instrumentation
- écrire un agent `java.lang.instrument` (ByteBuddy ou ASM)
- mesurer overhead
- corréler traces (APM) et profiling bas niveau

## Semaines 17-20 — JIT avancé + perf patterns
- inlining / devirtualization (concepts)
- escape analysis (approche pratique)
- anti-patterns (boxing, alloc storms, contention)

## Semaines 21-24 — Production JVM sur K8s/OpenShift
- patterns flags JVM (MaxRAMPercentage, GC logs, dumps)
- readiness/liveness + graceful shutdown
- observabilité (JFR continu, export, rotation)
- drill incidents (CPU throttling, OOMKill, latence p99)
