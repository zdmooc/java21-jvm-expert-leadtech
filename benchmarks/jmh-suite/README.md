# JMH Suite

Build:
```bash
mvn -q -pl benchmarks/jmh-suite -am package
```

Run:
```bash
java -jar benchmarks/jmh-suite/target/jmh-suite.jar -wi 3 -i 5 -f 2
# filtre
java -jar benchmarks/jmh-suite/target/jmh-suite.jar StringConcatBench
```

Règles :
- Toujours warmup + forks.
- Ne jamais tirer des conclusions “prod” avec un seul run.
- Le but ici = apprendre les biais (DCE, warmup, inlining, etc.).
