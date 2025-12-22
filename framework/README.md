# Module optionnel "framework" (Spring Boot, Quarkus, OpenShift, Dynatrace)

But : garder **les mêmes scénarios JVM** (CPU/alloc/sleep/lock/leak/direct + métriques) tout en changeant :
- le framework runtime (**Spring Boot** vs **Quarkus**)
- la cible de déploiement (**OpenShift**)
- l’observabilité (**Dynatrace** : injection Operator / OpenTelemetry OTLP)

## 1) Build & run

### Spring Boot
```bash
mvn -Pframework-spring -pl framework/springboot-lab -am package
java -jar framework/springboot-lab/target/springboot-lab.jar
```

Endpoints (identiques au lab HTTP) :
- `/health`
- `/cpu?ms=200`
- `/alloc?mb=10`
- `/sleep?ms=200`
- `/lock?key=global&holdMs=200`
- `/leak?mb=50`
- `/direct?mb=50`
- `/metrics`

Actuator/Prometheus :
- `/actuator/health`
- `/actuator/prometheus`

Virtual threads (Spring Boot 3.2+) :
- `spring.threads.virtual.enabled=true` (déjà activé par défaut ici, modifiable).

### Quarkus
```bash
mvn -Pframework-quarkus -pl framework/quarkus-lab -am package
java -jar framework/quarkus-lab/target/quarkus-app/quarkus-run.jar
```

Quarkus expose par défaut :
- santé : `/q/health`
- métriques : `/q/metrics`

Virtual threads (Quarkus) :
- usage explicite de `@RunOnVirtualThread` sur les endpoints qui bloquent.

## 2) Déploiement OpenShift

Manifests :
- `framework/openshift/springboot/`
- `framework/openshift/quarkus/`

Principes :
- image attendue dans un registry (interne OpenShift ou externe)
- `Route` exposée en HTTPS (TLS edge par défaut)
- endpoints identiques aux labs

## 3) Dynatrace

Runbook : `framework/observability/dynatrace/README.md`

Approches :
1) **Dynatrace Operator** (injection automatique / sélective via annotations)
2) **OpenTelemetry OTLP** vers Dynatrace (Collector ou direct exporter)
