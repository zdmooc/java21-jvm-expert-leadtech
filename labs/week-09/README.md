    # Week 09 — Heap dump + MAT : leak triage (≈10h)

    ## Objectif
    - Provoquer une fuite mémoire contrôlée.
- Faire une analyse MAT (dominators, retained size).

    ## Plan (10h)
    - 2h: rappel OOM
- 7h: leak + dump + analyse
- 1h: runbook

    ## Labs (commandes)
    ```bash
HEAP=256m ./scripts/run-server.sh

# Ajoute 50MB puis 50MB...
curl "http://localhost:8080/leak?action=add&mb=50"
curl "http://localhost:8080/leak?action=add&mb=50"
curl "http://localhost:8080/leak?action=count"

# Heap dump
./scripts/take-heap-dump.sh
# Analyse dans Eclipse MAT (manuel)
```

    ## Livrables (à commit)
    - capture écran MAT (à mettre dans evidence si tu veux)
- conclusion cause
- commit `week-09`
