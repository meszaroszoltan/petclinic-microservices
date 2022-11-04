package org.micronautframework.samples.petclinic.visits.web;

import io.micronaut.http.HttpMessage;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import org.micronautframework.samples.petclinic.visits.model.Visit;
import org.micronautframework.samples.petclinic.visits.model.VisitRepository;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
public class VisitResource {

    private final VisitRepository visitRepository;

    public VisitResource(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;

        visitRepository.saveAll(List.of(
            new Visit(new Date(), "rabies shot", 7),
            new Visit(new Date(), "rabies shot", 8),
            new Visit(new Date(), "neutered", 8),
            new Visit(new Date(), "spayed", 7)
        ));
    }

    @Post("owners/{ownerId}/pets/{petId}/visits")
    HttpMessage<Visit> create(
        @Valid @Body Visit visit,
        @PathVariable("ownerId") int ownerId,
        @PathVariable("petId") int petId
    ) {
        visit.setPetId(petId);
        var created = visitRepository.save(visit);
        return HttpResponse.status(HttpStatus.CREATED).body(created);
    }

    @Get("owners/{ownerId}/pets/{petId}/visits")
    List<Visit> visits(
        @PathVariable("ownerId") int ownerId,
        @PathVariable("petId") int petId
    ) {
        return visitRepository.findByPetId(petId);
    }

    @Get("pets/visits")
    Visits visitsMultiGet(@QueryValue("ids") List<Integer> petIds) {
        final List<Visit> byPetIdIn = visitRepository.findByPetIdIn(petIds);
        return new Visits(byPetIdIn);
    }

    static class Visits {
        private final List<Visit> items;

        Visits(List<Visit> items) {
            this.items = items;
        }

        public List<Visit> getItems() {
            return items;
        }
    }
}
