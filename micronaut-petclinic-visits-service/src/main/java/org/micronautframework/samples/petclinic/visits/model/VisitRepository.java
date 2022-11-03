package org.micronautframework.samples.petclinic.visits.model;

import io.micronaut.context.annotation.Executable;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Slice;
import io.micronaut.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

@Repository
interface BookRepository extends CrudRepository<Visit, Long> { //
    @Executable
    List<Visit> findByPetId(int petId);

    List<Visit> findByPetIdIn(Collection<Integer> petIds);
}
