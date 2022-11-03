package org.micronautframework.samples.petclinic.customers.model;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface OwnerRepository extends CrudRepository<Owner, Long> {
}
