    # Week 02 — HTTP client + robustesse (timeouts, erreurs, design) (≈10h)

    ## Objectif
    - Renforcer la maîtrise API standard (HttpClient) et de la robustesse (timeouts, retries contrôlés).

    ## Plan (10h)
    - 2h: lire `tools/loadgen` et comprendre le design
- 6h: améliorer le loadgen (params, logs)
- 2h: documenter + commit

    ## Labs (commandes)
    ```bash
# Lancer le serveur
./scripts/run-server.sh

# Exécuter le loadgen
./scripts/run-load.sh http://localhost:8080/sleep?ms=50 200 20 virtual

# Exercice: ajouter un paramètre CLI "timeoutSeconds" au loadgen (option)
```

    ## Livrables (à commit)
    - notes `notes/week-02.md`
- amélioration loadgen (option)
- commit `week-02`
