package org.micronautframework.samples.petclinic.vets.web;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import org.micronautframework.samples.petclinic.vets.model.Specialty;
import org.micronautframework.samples.petclinic.vets.model.Vet;
import org.micronautframework.samples.petclinic.vets.model.VetRepository;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller("/vets")
class VetResource {

    private final VetRepository vetRepository;

    public VetResource(VetRepository vetRepository) {
        this.vetRepository = vetRepository;

        // ugly, I know
        Specialty radiology = new Specialty("radiology");
        Specialty surgery = new Specialty("surgery");
        Specialty dentistry = new Specialty("dentistry");

        vetRepository.saveAll(List.of(
            new Vet("James", "Carter", Collections.emptySet()),
            new Vet("Helen", "Leary", Set.of(radiology)),
            new Vet("Linda", "Douglas", Set.of(surgery, dentistry)),
            new Vet("Rafael", "Ortega", Set.of(radiology)),
            new Vet("Henry", "Stevens", Set.of(surgery)),
            new Vet("Sharon", "Jenkins", Set.of(radiology))
        ));

    }

    @Get
    public Iterable<Vet> showResourcesVetList() {
        return vetRepository.findAll();
    }
}
