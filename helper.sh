#!/usr/bin/env bash

spring_build_api() {
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/spring-petclinic-api-gateway
  ./mvnw spring-boot:build-image -DskipTests
  minikube image load spring-petclinic-cloud-api-gateway
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/
}

spring_build_visits() {
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/spring-petclinic-visits-service
  ./mvnw spring-boot:build-image -DskipTests
  minikube image load spring-petclinic-cloud-visits-service
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/
}

spring_build_vets() {
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/spring-petclinic-vets-service
  ./mvnw spring-boot:build-image -DskipTests
  minikube image load spring-petclinic-cloud-vets-service
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/
}

spring_build_customers() {
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/spring-petclinic-customers-service
  ./mvnw spring-boot:build-image -DskipTests
  minikube image load spring-petclinic-cloud-customers-service
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/
}

spring_build() {
  spring_build_api
  spring_build_visits
  spring_build_vets
  spring_build_customers
}

spring_deploy() {
  cd /mnt/c/Users/zolim/IdeaProjects/spring-petclinic-cloud/
  kubectl replace -f k8s --force
  kubectl replace -f k8s/spring/vets-service.yaml --force
  kubectl replace -f k8s/spring/visits-service.yaml --force
  kubectl replace -f k8s/spring/customers-service.yaml --force
  kubectl get all
}

expose() {
    minikube service api-gateway --url
}

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
