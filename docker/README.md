# Docker

Build + run:
```bash
mvn -q -pl apps/jvm-lab-server -am package
docker compose -f docker/docker-compose.yml up --build
```

Test:
```bash
curl "http://localhost:8080/health"
```
