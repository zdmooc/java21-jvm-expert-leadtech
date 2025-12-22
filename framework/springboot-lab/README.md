# springboot-lab (Java 21)

## Build
```bash
mvn -Pframework-spring -pl framework/springboot-lab -am package
```

## Run
```bash
java -jar target/springboot-lab.jar
```

## Endpoints lab JVM
Identiques au `apps/jvm-lab-server` :
- `/health`
- `/cpu?ms=200`
- `/alloc?mb=10`
- `/sleep?ms=200`
- `/lock?key=global&holdMs=200`
- `/leak?mb=50`
- `/direct?mb=50`
- `/metrics`

## Observabilité
- Actuator health : `/actuator/health`
- Prometheus : `/actuator/prometheus`

## Virtual Threads
Activé via :
`spring.threads.virtual.enabled=true`

Référence : Spring Boot + virtual threads (3.2+) (voir doc/ressources dans `framework/observability`).
