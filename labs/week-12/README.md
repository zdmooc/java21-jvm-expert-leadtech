    # Week 12 — Capstone : containers/K8s + incident drill (≈10h)

    ## Objectif
    - Déployer le serveur en container/K8s.
- Diagnostiquer un incident (throttling, OOMKill, latence).

    ## Plan (10h)
    - 8h: capstone
- 2h: postmortem + runbooks

    ## Labs (commandes)
    ```bash
# Docker
mvn -q -pl apps/jvm-lab-server -am package
docker build -t jvm-lab-server:1.0.0 -f docker/Dockerfile.jvm-lab-server .
docker compose -f docker/docker-compose.yml up

# K8s (option)
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/deploy-jvm-lab-server.yaml
kubectl apply -f k8s/svc-jvm-lab-server.yaml
kubectl -n jvm-labs port-forward svc/jvm-lab-server 8080:8080
```

    ## Livrables (à commit)
    - postmortem rempli (`templates/postmortem.md`)
- runbooks finalisés
- tag git `v1.0-learning` (option)
