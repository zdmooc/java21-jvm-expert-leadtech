# Lead Tech — Checklist review (Java/JVM)

## Code
- Exceptions: pas de catch silencieux, messages utiles
- Logging: corrélation, pas de logs verbeux en hot path
- Concurrence: pas de locks globaux, pas de pools infinis
- Collections/caches: bornés, eviction claire
- I/O: timeouts explicites (HTTP/DB)
- Encodage/locale: pas de dépendance implicite

## Performance
- Mesure: JMH/JFR/profilers avant optimisation
- Allocation: éviter storms, boxing, conversions inutiles
- Latence: p99 pris en compte (pas seulement moyenne)
- GC: logs activables, heap dumps activables

## Prod/K8s
- readiness/liveness OK
- graceful shutdown
- resources requests/limits cohérents
- observabilité (JFR on-demand, metrics)
