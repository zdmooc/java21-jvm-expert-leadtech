# jcmd — commandes utiles (prod/labs)

Lister JVM:
```bash
jcmd
```

Thread dump:
```bash
jcmd <pid> Thread.print -l
```

Heap info:
```bash
jcmd <pid> GC.heap_info
```

Heap dump:
```bash
jcmd <pid> GC.heap_dump /path/heap.hprof
```

Flags:
```bash
jcmd <pid> VM.flags
jcmd <pid> VM.system_properties
```

JFR:
```bash
jcmd <pid> JFR.start name=lab settings=profile duration=60s filename=lab.jfr
jcmd <pid> JFR.stop name=lab
```
