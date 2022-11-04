package org.micronautframework.samples.petclinic.customers.web;

import io.micronaut.http.HttpMessage;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import org.micronautframework.samples.petclinic.customers.model.Owner;
import org.micronautframework.samples.petclinic.customers.model.OwnerRepository;
import org.micronautframework.samples.petclinic.customers.model.Pet;
import org.micronautframework.samples.petclinic.customers.model.PetRepository;
import org.micronautframework.samples.petclinic.customers.model.PetType;
import org.micronautframework.samples.petclinic.customers.model.PetTypeRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
class PetResource {
    private final PetRepository petRepository;
    private final OwnerRepository ownerRepository;
    private final PetTypeRepository petTypeRepository;

    public PetResource(PetRepository petRepository, OwnerRepository ownerRepository, PetTypeRepository petTypeRepository) {
        this.petRepository = petRepository;
        this.ownerRepository = ownerRepository;
        this.petTypeRepository = petTypeRepository;

        var owner = new Owner(null, "first", "last", "addr", "city", "123" , Collections.emptySet());
        var type = new PetType(null, "cat");
        var pet = new Pet(null, "name", "new Date()", type, owner);

        ownerRepository.save(owner);
        petTypeRepository.save(type);
        petRepository.save(pet);
        for (Owner owner1 : ownerRepository.findAll()) {
            System.out.println(owner1);
        }
    }

    @Get("/petTypes")
    public List<PetType> getPetTypes() {
        return petRepository.findPetTypes();
    }

    @Post("/owners/{ownerId}/pets")
    public HttpMessage<Pet> processCreationForm(
        @Body PetRequest petRequest,
        @PathVariable("ownerId") int ownerId
    ) {
        final Pet pet = new Pet();
        final Optional<Owner> optionalOwner = ownerRepository.findById(ownerId);
        Owner owner = optionalOwner.orElseThrow(() -> new RuntimeException("Owner " + ownerId + " not found"));
        owner.addPet(pet);

        return HttpResponse.status(HttpStatus.CREATED).body(save(pet, petRequest));
    }

    @Put("/owners/{ownerId}/pets/{petId}")
    public HttpMessage<Object> processUpdateForm(
        @PathVariable("ownerId") int ownerId,
        @PathVariable("petId") int petId,
        @Body PetRequest petRequest
    ) {
        Pet pet = findPetById(petId);
        pet.setName(petRequest.getName());
        pet.setBirthDate(petRequest.getBirthDate());
        return HttpResponse.status(HttpStatus.NO_CONTENT);
    }

    private Pet save(final Pet pet, final PetRequest petRequest) {
        pet.setName(petRequest.getName());
        pet.setBirthDate(petRequest.getBirthDate());

        petRepository
            .findPetTypeById(petRequest.getTypeId())
            .ifPresent(pet::setType);

        return petRepository.save(pet);
    }

    @Get("owners/{ownerId}/pets/{petId}")
    public PetDetails findPet(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId) {
        return new PetDetails(findPetById(petId));
    }


    private Pet findPetById(int petId) {
        Optional<Pet> pet = petRepository.findById(petId);
        if (pet.isEmpty()) {
            throw new RuntimeException("Pet " + petId + " not found");
        }
        return pet.get();
    }

}
