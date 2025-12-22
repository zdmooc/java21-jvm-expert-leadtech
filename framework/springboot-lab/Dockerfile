# Build local préalable (recommandé) :
#   mvn -Pframework-spring -pl framework/springboot-lab -am package
# puis :
#   podman build -t springboot-lab:latest .

FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:latest

WORKDIR /app
COPY target/springboot-lab.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
