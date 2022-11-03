package org.micronautframework.samples.petclinic.vets.model;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface VetRepository extends CrudRepository<Vet, Long> {
}
