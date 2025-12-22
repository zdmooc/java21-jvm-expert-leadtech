    # Week 05 — Bytecode + classloading (≈10h)

    ## Objectif
    - Comprendre `.class`, `javap`, classloading/metaspace.
- Savoir interpréter un bytecode simple.

    ## Plan (10h)
    - 3h: lecture + notes
- 6h: labs javap + mini exp
- 1h: synthèse

    ## Labs (commandes)
    ```bash
# Inspecter bytecode
javap -c -v apps/jvm-lab-server/target/classes/io/zd/jvm/server/Handlers.class | less

# Lancer serveur et inspecter classloading (jcmd)
./scripts/run-server.sh
PID=$(./scripts/jcmd-pid.sh)
jcmd "$PID" VM.class_hierarchy | head -n 40 || true
jcmd "$PID" GC.class_histogram | head -n 40
```

    ## Livrables (à commit)
    - notes bytecode + classloading
- 1 page: 'ce que je dois retenir'
- commit `week-05`
