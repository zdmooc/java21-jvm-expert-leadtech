# Kubernetes labs (Kind/Minikube)

## Build image locally
```bash
mvn -q -pl apps/jvm-lab-server -am package
docker build -t jvm-lab-server:1.0.0 -f docker/Dockerfile.jvm-lab-server .
```

## Deploy
```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/deploy-jvm-lab-server.yaml
kubectl apply -f k8s/svc-jvm-lab-server.yaml
kubectl -n jvm-labs port-forward svc/jvm-lab-server 8080:8080
```

## Test
```bash
curl "http://localhost:8080/alloc?mb=5"
```

Objectif K8s : observer CPU throttling / memory limits / comportements GC sous cgroups.
