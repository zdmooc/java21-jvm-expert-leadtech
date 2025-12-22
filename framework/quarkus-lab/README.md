# quarkus-lab (Java 21)

## Build
```bash
mvn -Pframework-quarkus -pl framework/quarkus-lab -am package
```

## Run
```bash
java -jar target/quarkus-app/quarkus-run.jar
```

## Endpoints lab JVM
- `/health`
- `/cpu?ms=200`
- `/alloc?mb=10`
- `/sleep?ms=200`
- `/lock?key=global&holdMs=200`
- `/leak?mb=50`
- `/direct?mb=50`
- `/metrics`

## Quarkus endpoints
- santé : `/q/health`
- métriques Prometheus : `/q/metrics`

## Virtual Threads
Quarkus exécute les handlers annotés `@RunOnVirtualThread` sur des virtual threads (utile pour code bloquant).
