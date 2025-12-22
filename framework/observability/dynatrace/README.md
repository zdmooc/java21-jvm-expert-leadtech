# Dynatrace sur OpenShift pour les modules "framework"

Ce dossier propose 2 chemins compatibles OpenShift :

1) **Dynatrace Operator / OneAgent injection**
2) **OpenTelemetry OTLP** (direct ou via Collector) vers Dynatrace

## 1) Dynatrace Operator (OneAgent)

### Injection automatique (recommandé)
- Installer le **Dynatrace Operator** et créer un `DynaKube`.
- Activer **application monitoring** + (optionnel) **cloud native fullstack**.
- L’injection OneAgent se fait automatiquement selon la configuration du `DynaKube` (selector / namespaceSelector).

### Injection sélective (par annotation)
Si vous avez désactivé l’injection automatique dans le `DynaKube`, vous pouvez activer l’injection par Pod/Deployment via :

```yaml
metadata:
  annotations:
    oneagent.dynatrace.com/inject: "true"
```

Les manifests OpenShift fournis (`framework/openshift/*/deployment.yaml`) contiennent déjà le placeholder commenté.

## 2) OpenTelemetry OTLP vers Dynatrace

### Option A — exporter OTLP directement depuis l’application
Définir :
- `OTEL_EXPORTER_OTLP_ENDPOINT` (ex: `https://{your-environment-id}.live.dynatrace.com/api/v2/otlp`)
- `OTEL_EXPORTER_OTLP_HEADERS` (ex: `Authorization=Api-Token dt0c01...`)

> Remarque : ce mode suppose que votre application est instrumentée OpenTelemetry (agent auto-instrumentation Java ou SDK).

### Option B — OpenTelemetry Collector (recommandé)
- Les apps envoient vers un Collector local (sidecar/daemonset/service).
- Le Collector exporte vers Dynatrace via `otlphttp` avec header `Authorization: Api-Token ...`.

Fichier exemple : `otel-collector-config.yaml`

## Fichiers fournis
- `dynatrace-otlp-secret.yaml` : secret exemple pour stocker l’Authorization header.
- `otel-collector-config.yaml` : config de Collector minimale pour exporter traces/metrics/logs vers Dynatrace.
