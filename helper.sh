#!/usr/bin/env bash

ROOT=$(pwd)

# Infra
setup() {
  build_infra
  deploy_infra
  expose
}

build_infra() {
  cd $ROOT/spring-petclinic-api-gateway
  ./mvnw spring-boot:build-image -DskipTests
  minikube image load spring-petclinic-cloud-api-gateway
  cd $ROOT/
}

deploy_infra() {
  cd $ROOT/
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
  cd $ROOT/spring-petclinic-visits-service
  ./mvnw spring-boot:build-image -DskipTests
  cd $ROOT/
}

build_spring_customers() {
  cd $ROOT/spring-petclinic-customers-service
  ./mvnw spring-boot:build-image -DskipTests
  cd $ROOT/
}

build_spring_vets() {
  cd $ROOT/spring-petclinic-vets-service
  ./mvnw spring-boot:build-image -DskipTests
  cd $ROOT/
}

deploy_spring() {
  cd $ROOT/

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
  cd $ROOT/micronaut-petclinic-visits-service
  ./mvnw package -Dpackaging=docker
   cd $ROOT/
}

build_micro_jvm_vets() {
  cd $ROOT/micronaut-petclinic-vets-service
  ./mvnw package -Dpackaging=docker
  cd $ROOT/
}

build_micro_jvm_customers() {
  cd $ROOT/micronaut-petclinic-customers-service
  ./mvnw package -Dpackaging=docker
  cd $ROOT/
}

build_micro_native_visits() {
  cd $ROOT/micronaut-petclinic-visits-service
  ./mvnw package -Dmicronaut.aot.enabled=true -Dpackaging=docker-native
  cd $ROOT/
}

build_micro_native_vets() {
  cd $ROOT/micronaut-petclinic-vets-service
  ./mvnw package -Dmicronaut.aot.enabled=true -Dpackaging=docker-native
  cd $ROOT/
}

build_micro_native_customers() {
  cd $ROOT/micronaut-petclinic-customers-service
  ./mvnw package -Dmicronaut.aot.enabled=true -Dpackaging=docker-native
  cd $ROOT/
}

deploy_micro_jvm() {
  deploy_micro
}

deploy_micro_native() {
  deploy_micro
}

deploy_micro() {
  cd $ROOT/

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

build_quarkus_jvm() {
  build_quarkus_jvm_vets
  build_quarkus_jvm_visits
  build_quarkus_jvm_customers
}

build_quarkus_jvm_vets() {
  cd $ROOT/quarkus-petclininc-vets-service
  ./mvnw package -DskipTests
  docker build -f src/main/docker/Dockerfile.jvm -t quarkus/quarkus-petclinic-vets-service-jvm .
  cd $ROOT/
}

build_quarkus_jvm_visits() {
  cd $ROOT/quarkus-petclininc-visits-service
  ./mvnw package -DskipTests
  docker build -f src/main/docker/Dockerfile.jvm -t quarkus/quarkus-petclinic-visits-service-jvm .
  cd $ROOT/
}

build_quarkus_jvm_customers() {
  cd $ROOT/quarkus-petclininc-customers-service
  ./mvnw package -DskipTests
  docker build -f src/main/docker/Dockerfile.jvm -t quarkus/quarkus-petclinic-customers-service-jvm .
  cd $ROOT/
}

#build_quarkus_native_vets() {
#  cd $ROOT/quarkus-petclininc-vets-service
#  ./mvnw package -Pnative -DskipTests
#  docker build -f src/main/docker/Dockerfile.native-micro -t quarkus/code-with-quarkus .
#  cd $ROOT/
#}

deploy_quarkus() {
  cd $ROOT/

  minikube image load quarkus/quarkus-petclinic-visits-service-jvm:latest
  minikube image load quarkus/quarkus-petclinic-vets-service-jvm:latest
  minikube image load quarkus/quarkus-petclinic-customers-service-jvm:latest

  kubectl replace -f k8s/quarkus/visits-service.yaml --force
  kubectl replace -f k8s/quarkus/vets-service.yaml --force
  kubectl replace -f k8s/quarkus/customers-service.yaml --force

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
  read -n 1 -s -r -p "Press any key to continue"
  echo "The monitoring will terminate after 2 minutes"
  {
      sleep 2m
      kill $$
  } &
  cd ./measurements
  rm ./log
  test &
  while true
  do
      clear
      kubectl top po | tee -a ./log
      sleep 1
  done
  python3 convert.py
  echo "check the metrics.csv file for the results"
}

$1
$2
$3
