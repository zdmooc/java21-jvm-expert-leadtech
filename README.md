# Java 21 — Expert Lead Tech (JVM & Optimisation) — Repo complet

Objectif : suivre un parcours **10h/semaine** pour devenir **Lead Tech Java** orienté **JVM, performance, GC, JIT, profiling, troubleshooting** et exécution **containers/Kubernetes**.

Ce dépôt fournit :
- un **plan 12 semaines** (10h/semaine) + extension **24 semaines**
- une **application de lab** (serveur HTTP) avec endpoints pour provoquer CPU/alloc/locks/leaks
- un **load generator** (client) pour générer de la charge
- une **suite JMH** pour apprendre à benchmark correctement
- des **runbooks** (OOM, latence, CPU, deadlocks)
- des **cheatsheets** (jcmd, GC, JFR, flags)
- des scripts Bash (compatibles Git Bash) + Docker/K8s manifests

## Prérequis
- **JDK 21** installé (`java -version`).
- **Maven 3.9+**
- Git Bash (Windows) ou Bash (Linux/macOS)
- Optionnel : Docker + Kind/Minikube

## Quickstart (5 minutes)
```bash
# 1) Build
mvn -q -DskipTests package

# 2) Lancer le serveur (par défaut: virtual threads)
./scripts/run-server.sh

# 3) Dans un autre terminal : générer de la charge
./scripts/run-load.sh http://localhost:8080/alloc?mb=5 200 30
```

Endpoints utiles :
- `GET /health`
- `GET /cpu?ms=200`
- `GET /alloc?mb=10`
- `GET /sleep?ms=200`
- `GET /lock?ms=200&key=global`
- `GET /leak?mb=50`
- `GET /direct?mb=50`
- `GET /metrics` (compteurs/latences simples)

## Plan d'apprentissage (10h/semaine)
- Plan 12 semaines : `docs/ROADMAP_12W.md`
- Plan 24 semaines (approfondissement) : `docs/ROADMAP_24W.md`
- Chaque semaine : `labs/week-XX/README.md`

## Structure
```
apps/jvm-lab-server/        # serveur de lab (Java 21)
tools/loadgen/              # générateur de charge (Java 21)
benchmarks/jmh-suite/        # benchmarks JMH
labs/week-01..12/            # consignes + objectifs + temps
docs/                         # roadmaps + cheatsheets + runbooks
scripts/                      # scripts d’exécution
docker/                       # Dockerfile + compose
k8s/                          # manifests Kubernetes
templates/                    # modèles postmortem / perf test plan / review
```

## Philosophie “Lead Tech JVM”
- Mesurer avant d’optimiser (JMH/JFR/profilers)
- Comprendre GC/JIT/mémoire (et pas seulement “augmenter le heap”)
- Tuning minimal, orienté symptômes, reproductible
- Runbooks clairs + evidence (logs, dumps, profiles)

---
Si tu veux, je peux adapter ce dépôt à ton contexte (Spring Boot, Quarkus, OpenShift, Dynatrace) en ajoutant un module optionnel “framework”.

## Modules optionnels (framework)
Objectif : réutiliser les **mêmes labs JVM** (CPU/alloc/locks/leaks/JFR) avec des stacks **Spring Boot**, **Quarkus**, et les déployer sur **OpenShift** avec instrumentation **Dynatrace**.

- Vue d’ensemble + commandes : `framework/README.md`
- Spring Boot (Java 21) : `framework/springboot-lab/`
- Quarkus (Java 21, @RunOnVirtualThread) : `framework/quarkus-lab/`
- OpenShift manifests (Service/Route/Deployment) : `framework/openshift/`
- Dynatrace (Operator injection + OTLP) : `framework/observability/dynatrace/`

Build (au choix) :
```bash
# Spring Boot
mvn -Pframework-spring -pl framework/springboot-lab -am package

# Quarkus
mvn -Pframework-quarkus -pl framework/quarkus-lab -am package

# Les deux
mvn -Pframework-all package
```
