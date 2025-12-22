# Roadmap 12 semaines — 10h/semaine (Lead Tech Java 21 + JVM)

Chaque semaine = **~10h** :
- 3h théorie/lecture guidée (notes dans `notes/` ou `docs/`)
- 6h labs (code + mesures)
- 1h synthèse (runbook/checklist + commit)

## Semaine 1 — Java 21 moderne (langage/API)
Livrable : refactor DTO→records + switch/pattern matching + text blocks
- 2h : lecture `docs/cheatsheets/java8-to-21.md`
- 6h : `labs/week-01`
- 2h : notes + commit

## Semaine 2 — API réseau + structuration projet
Livrable : client HTTP + tests + structure propre (packaging, error handling)
- 4h : `labs/week-02`
- 4h : refacto + qualité (checkstyle mental: lisibilité, exceptions, logs)
- 2h : documenter

## Semaine 3 — Concurrence 1 : virtual threads (Loom)
Livrable : comparaison fixed pool vs virtual threads sur `/sleep` et `/alloc`
- 6h : `labs/week-03`
- 4h : notes + matrice “symptôme→cause→action”

## Semaine 4 — Concurrence 2 : contention, locks, thread dumps
Livrable : runbook deadlock/lock contention
- 6h : `labs/week-04`
- 4h : runbook `docs/runbooks/incident-locks.md` (compléter)

## Semaine 5 — JVM internals 1 : bytecode + classloading
Livrable : lecture `javap -c -v` + mini classloader (optionnel)
- 6h : `labs/week-05`
- 4h : notes + quiz perso

## Semaine 6 — JVM internals 2 : JPMS/modules + réflexion
Livrable : comprendre `--add-opens`, corriger sans flags si possible
- 6h : `labs/week-06`
- 4h : checklists compat 8→21

## Semaine 7 — Benchmarking correct : JMH
Livrable : exécuter suite JMH + écrire 1 benchmark perso
- 6h : `labs/week-07`
- 4h : notes + pièges

## Semaine 8 — Mémoire + GC (G1) + GC logs
Livrable : analyser GC logs + tuning minimal
- 7h : `labs/week-08`
- 3h : runbook OOM/GC

## Semaine 9 — Heap dumps + MAT : leaks et retained size
Livrable : heap dump sur `/leak` + analyse MAT
- 7h : `labs/week-09`
- 3h : runbook “memory leak triage”

## Semaine 10 — JFR (profiling prod-friendly)
Livrable : capture JFR + interprétation (CPU/alloc/locks)
- 7h : `labs/week-10`
- 3h : template incident perf

## Semaine 11 — async-profiler + flamegraphs
Livrable : profil CPU + alloc (flamegraph) + conclusion actionnable
- 7h : `labs/week-11`
- 3h : checklists “profiling”

## Semaine 12 — Capstone : JVM sous containers/K8s + drill incident
Livrable : postmortem complet + correctifs + runbooks finalisés
- 8h : `labs/week-12`
- 2h : consolidation repo (docs + tags)

> Extension 24 semaines : `docs/ROADMAP_24W.md`
