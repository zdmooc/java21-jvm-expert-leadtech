# Build local préalable (recommandé) :
#   mvn -Pframework-quarkus -pl framework/quarkus-lab -am package
# puis :
#   podman build -t quarkus-lab:latest .

FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:latest

WORKDIR /work
COPY target/quarkus-app/ /work/

EXPOSE 8080
CMD ["java","-jar","quarkus-run.jar"]
