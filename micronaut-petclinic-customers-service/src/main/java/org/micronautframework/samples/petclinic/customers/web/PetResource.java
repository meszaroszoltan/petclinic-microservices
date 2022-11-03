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

import java.util.List;
import java.util.Optional;

@Controller
class PetResource {
    private final PetRepository petRepository;
    private final OwnerRepository ownerRepository;

    public PetResource(PetRepository petRepository, OwnerRepository ownerRepository) {
        this.petRepository = petRepository;
        this.ownerRepository = ownerRepository;

        // TODO create entities
    }

    @Get("/petTypes")
    public List<PetType> getPetTypes() {
        return petRepository.findPetTypes();
    }

    @Post("/owners/{ownerId}/pets")
    public HttpMessage<Pet> processCreationForm(
        @Body PetRequest petRequest,
        @PathVariable("ownerId") long ownerId) {

        final Pet pet = new Pet();
        final Optional<Owner> optionalOwner = ownerRepository.findById(ownerId);
        Owner owner = optionalOwner.orElseThrow(() -> new RuntimeException("Owner " + ownerId + " not found"));
        owner.addPet(pet);

        return HttpResponse.status(HttpStatus.CREATED).body(save(pet, petRequest));
    }

    @Put("/owners/*/pets/*")
    public HttpMessage<Object> processUpdateForm(@Body PetRequest petRequest) {
        int petId = petRequest.getId();
        Pet pet = findPetById(petId);
        save(pet, petRequest);
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

    @Get("owners/*/pets/{petId}")
    public PetDetails findPet(@PathVariable("petId") int petId) {
        return new PetDetails(findPetById(petId));
    }


    private Pet findPetById(long petId) {
        Optional<Pet> pet = petRepository.findById(petId);
        if (pet.isEmpty()) {
            throw new RuntimeException("Pet " + petId + " not found");
        }
        return pet.get();
    }

}
