# OpenShift (CRC / OCP) — déploiement des modules "framework"

## Pré-requis
- `oc` connecté à votre cluster
- un namespace/projet dédié (ex: `jvm-lab`)
- build local déjà fait (afin d’avoir `target/` dans le contexte Docker)

## 1) Build des artefacts Java
Depuis la racine du dépôt :

```bash
# Spring Boot
mvn -Pframework-spring -pl framework/springboot-lab -am package

# Quarkus
mvn -Pframework-quarkus -pl framework/quarkus-lab -am package
```

## 2) Build d’images dans OpenShift (binary build + Dockerfile)
```bash
oc new-project jvm-lab

# Spring Boot (context = dossier module, Dockerfile inclut COPY target/*.jar)
oc new-build --name=springboot-lab --binary --strategy=docker
oc start-build springboot-lab --from-dir=framework/springboot-lab --follow

# Quarkus
oc new-build --name=quarkus-lab --binary --strategy=docker
oc start-build quarkus-lab --from-dir=framework/quarkus-lab --follow
```

## 3) Déployer les manifests (Service/Route/Deployment)
```bash
# via kustomize
oc apply -k framework/openshift/springboot
oc apply -k framework/openshift/quarkus

# récupérer les URLs
oc get route
```

## 4) Dynatrace
Voir `framework/observability/dynatrace/README.md`
