    # Week 08 — GC G1 + GC logs + tuning minimal (≈10h)

    ## Objectif
    - Lire GC logs, comprendre allocation rate, pauses.
- Appliquer du tuning minimal et justifié.

    ## Plan (10h)
    - 3h: bases G1
- 6h: labs alloc + analyse GC
- 1h: runbook OOM/GC

    ## Labs (commandes)
    ```bash
HEAP=256m ./scripts/run-server.sh
./scripts/run-load.sh "http://localhost:8080/alloc?mb=10&chunks=50" 300 30 virtual

# Ouvrir logs/gc.log et repérer : pauses, allocation rate, full GC
tail -n 200 logs/gc.log
```

    ## Livrables (à commit)
    - analyse GC logs (notes)
- runbook OOM/GC mis à jour
- commit `week-08`
