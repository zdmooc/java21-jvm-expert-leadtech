    # Week 04 — Contention locks + thread dumps + runbook (≈10h)

    ## Objectif
    - Diagnostiquer contention et deadlocks.
- Savoir lire un thread dump.

    ## Plan (10h)
    - 2h: lire `docs/runbooks/incident-locks.md`
- 6h: provoquer contention via `/lock`
- 2h: enrichir runbook + commit

    ## Labs (commandes)
    ```bash
./scripts/run-server.sh
./scripts/run-load.sh "http://localhost:8080/lock?ms=100&key=global" 400 30 virtual

# Capturer 3 dumps
./scripts/take-thread-dump.sh
sleep 5
./scripts/take-thread-dump.sh
sleep 5
./scripts/take-thread-dump.sh
```

    ## Livrables (à commit)
    - 3 thread dumps + analyse dans notes
- runbook locks amélioré
- commit `week-04`
