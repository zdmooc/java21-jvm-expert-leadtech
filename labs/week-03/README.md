    # Week 03 — Virtual threads (Loom) : charge I/O (≈10h)

    ## Objectif
    - Comprendre différence fixed pool vs virtual threads.
- Mesurer RPS/latence sur `/sleep` et `/alloc`.

    ## Plan (10h)
    - 3h: lire docs Loom (JEP 444) + notes
- 6h: exécuter comparaisons
- 1h: synthèse + commit

    ## Labs (commandes)
    ```bash
# Mode virtual threads (défaut)
SERVER_EXECUTOR=virtual ./scripts/run-server.sh

# Charge I/O
./scripts/run-load.sh "http://localhost:8080/sleep?ms=50" 500 30 virtual

# Mode fixed pool
SERVER_EXECUTOR=fixed SERVER_FIXED_POOL=200 ./scripts/run-server.sh
./scripts/run-load.sh "http://localhost:8080/sleep?ms=50" 500 30 fixed

# Capturer thread dumps en charge
./scripts/take-thread-dump.sh
```

    ## Livrables (à commit)
    - tableau résultats (notes)
- conclusion: quand virtual threads aident / quand pas
- commit `week-03`
