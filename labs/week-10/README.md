    # Week 10 — JFR : profiling prod-friendly (≈10h)

    ## Objectif
    - Capturer une JFR 60s sous charge.
- Extraire conclusions CPU/alloc/locks.

    ## Plan (10h)
    - 3h: lire `docs/cheatsheets/jcmd.md`
- 6h: labs JFR
- 1h: template incident

    ## Labs (commandes)
    ```bash
./scripts/run-server.sh
./scripts/run-load.sh "http://localhost:8080/alloc?mb=5" 200 20 virtual

# JFR 60s (pendant charge)
./scripts/start-jfr.sh perf 60s
# Ouvrir le .jfr dans JDK Mission Control (JMC)
```

    ## Livrables (à commit)
    - 1 page synthèse JFR
- commit `week-10`
