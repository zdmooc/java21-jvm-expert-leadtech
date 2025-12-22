# Cheatsheet — Java 8 → Java 21 (points qui changent vraiment)

## Langage
- records, sealed classes
- switch expressions + pattern matching switch
- instanceof pattern
- text blocks, var

## Concurrence
- virtual threads (Loom) : I/O bloquante plus scalable
- structured concurrency (preview) : regrouper/annuler proprement

## Plateforme
- JPMS (modules) et encapsulation : réflexion “illégale” → `--add-opens` temporaire
- APIs retirées du JDK (JAXB/JAX-WS, Nashorn…) → dépendances externes

## Runtime
- meilleure compat containers/cgroups
- GC mature (G1), options modernes selon distrib
- outillage : JFR, jcmd, etc.

## Risques migration
- dépendances legacy (frameworks/plugins)
- agents APM / instrumentation bytecode
- TLS/ciphers
- encodage (UTF-8 par défaut depuis 18)
