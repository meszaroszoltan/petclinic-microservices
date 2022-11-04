package org.micronautframework.samples.petclinic.visits.model;

import io.micronaut.context.annotation.Executable;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

@Repository
public interface VisitRepository extends CrudRepository<Visit, Integer> {
    @Executable
    List<Visit> findByPetId(int petId);

    List<Visit> findByPetIdIn(Collection<Integer> petIds);
}
