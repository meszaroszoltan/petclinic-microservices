#!/bin/bash
docker push spring-petclinic-cloud-api-gateway:latest
docker push spring-petclinic-cloud-visits-service:latest
docker push spring-petclinic-cloud-vets-service:latest
docker push spring-petclinic-cloud-customers-service:latest
docker push spring-petclinic-cloud-admin-server:latest
docker push spring-petclinic-cloud-discovery-service:latest
docker push spring-petclinic-cloud-config-server:latest

