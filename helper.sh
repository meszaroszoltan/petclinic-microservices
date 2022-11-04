#!/usr/bin/env bash

# Infra
setup() {
  build_infra
  deploy_infra
  expose
}

build_infra() {
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/spring-petclinic-api-gateway
  ./mvnw spring-boot:build-image -DskipTests
  minikube image load spring-petclinic-cloud-api-gateway
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/
}

deploy_infra() {
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/
  kubectl replace -f k8s --force
  kubectl replace -f k8s/spring/spring-petclinic-config.yaml --force
}

expose() {
  minikube service api-gateway --url
}

# Spring

build_spring() {
  build_spring_visits
  build_spring_customers
  build_spring_vets
}

build_spring_visits() {
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/spring-petclinic-visits-service
  ./mvnw spring-boot:build-image -DskipTests
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/
}

build_spring_customers() {
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/spring-petclinic-customers-service
  ./mvnw spring-boot:build-image -DskipTests
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/
}

build_spring_vets() {
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/spring-petclinic-vets-service
  ./mvnw spring-boot:build-image -DskipTests
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/
}

deploy_spring() {
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/

  minikube image load spring-petclinic-cloud-visits-service:latest
  minikube image load spring-petclinic-cloud-customers-service:latest
  minikube image load spring-petclinic-cloud-vets-service:latest

  kubectl replace -f k8s/spring/vets-service.yaml --force
  kubectl replace -f k8s/spring/visits-service.yaml --force
  kubectl replace -f k8s/spring/customers-service.yaml --force

  kubectl get all
}

# Micronaut

build_micro_jvm() {
  build_micro_jvm_visits
  build_micro_jvm_vets
  build_micro_jvm_customers
}

build_micro_native() {
  build_micro_native_visits
  build_micro_native_vets
  build_micro_native_customers
}

build_micro_jvm_visits() {
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/micronaut-petclinic-visits-service
  ./mvnw package -Dpackaging=docker
   cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/
}

build_micro_jvm_vets() {
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/micronaut-petclinic-vets-service
  ./mvnw package -Dpackaging=docker
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/
}

build_micro_jvm_customers() {
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/micronaut-petclinic-customers-service
  ./mvnw package -Dpackaging=docker
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/
}

build_micro_native_visits() {
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/micronaut-petclinic-visits-service
  ./mvnw package -Dmicronaut.aot.enabled=true -Dpackaging=docker-native
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/
}

build_micro_native_vets() {
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/micronaut-petclinic-vets-service
  ./mvnw package -Dmicronaut.aot.enabled=true -Dpackaging=docker-native
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/
}

build_micro_native_customers() {
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/micronaut-petclinic-customers-service
  ./mvnw package -Dmicronaut.aot.enabled=true -Dpackaging=docker-native
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/
}

deploy_micro_jvm() {
  deploy_micro
}

deploy_micro_native() {
  deploy_micro
}

deploy_micro() {
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/

  minikube image load micronaut-petclinic-visits-service:latest
  minikube image load micronaut-petclinic-customers-service:latest
  minikube image load micronaut-petclinic-vets-service:latest

  kubectl replace -f k8s/micronaut/vets-service.yaml --force
  kubectl replace -f k8s/micronaut/visits-service.yaml --force
  kubectl replace -f k8s/micronaut/customers-service.yaml --force
  kubectl replace -f k8s/micronaut/micronaut-petclinic-config.yaml --force

  kubectl get all
}

# Quarkus

build_micro_jvm() {
  build_quarkus_jvm_vets
  build_quarkus_jvm_visits

}

build_quarkus_jvm_vets() {
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/quarkus-petclininc-vets-service
  ./mvnw package -DskipTests
  docker build -f src/main/docker/Dockerfile.jvm -t quarkus/quarkus-petclinic-vets-service-jvm .
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/
}

build_quarkus_jvm_visits() {
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/quarkus-petclininc-visits-service
  ./mvnw package -DskipTests
  docker build -f src/main/docker/Dockerfile.jvm -t quarkus/quarkus-petclinic-visits-service-jvm .
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/
}


#build_quarkus_native_vets() {
#  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/quarkus-petclininc-vets-service
#  ./mvnw package -Pnative -DskipTests
#  docker build -f src/main/docker/Dockerfile.native-micro -t quarkus/code-with-quarkus .
#  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/
#}

deploy_quarkus() {
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/

  minikube image load quarkus/quarkus-petclinic-visits-service-jvm:latest
  minikube image load quarkus/quarkus-petclinic-vets-service-jvm:latest

  kubectl replace -f k8s/quarkus/visits-service.yaml --force
  kubectl replace -f k8s/quarkus/vets-service.yaml --force

  kubectl get all
}

# Testing

test() {
  cd measurements
  k6 run load-test.js
  cd ..
}

monitor() {
  #watch -n 0.5 'kubectl top po'
  cd ./measurements
  rm ./log
  while true
  do
      clear
      kubectl top po | tee -a ./log
      sleep 1
  done
}

$1
$2
$3
