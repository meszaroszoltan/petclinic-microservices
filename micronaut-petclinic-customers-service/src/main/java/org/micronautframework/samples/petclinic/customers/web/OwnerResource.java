package org.micronautframework.samples.petclinic.customers.web;

import io.micronaut.context.annotation.Bean;
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

import javax.validation.Valid;
import java.util.Optional;

@Controller("/owners")
class OwnerResource {

    private final OwnerRepository ownerRepository;

    public OwnerResource(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Post
    public HttpMessage<Owner> createOwner(@Valid @Body Owner owner) {
        var saved = ownerRepository.save(owner);
        return HttpResponse.status(HttpStatus.CREATED).body(saved);
    }

    @Get(value = "/{ownerId}")
    public Optional<Owner> findOwner(@PathVariable("ownerId") int ownerId) {
        return ownerRepository.findById(ownerId);
    }

    @Get
    public Iterable<Owner> findAll() {
        return ownerRepository.findAll();
    }

    @Put(value = "/{ownerId}")
    public HttpMessage<Object> updateOwner(@PathVariable("ownerId") int ownerId, @Valid @Body Owner ownerRequest) {
        final Optional<Owner> owner = ownerRepository.findById(ownerId);

        final Owner ownerModel = owner.orElseThrow(() -> new RuntimeException("Owner " + ownerId + " not found"));
        // This is done by hand for simplicity purpose. In a real life use-case we should consider using MapStruct.
        ownerModel.setFirstName(ownerRequest.getFirstName());
        ownerModel.setLastName(ownerRequest.getLastName());
        ownerModel.setCity(ownerRequest.getCity());
        ownerModel.setAddress(ownerRequest.getAddress());
        ownerModel.setTelephone(ownerRequest.getTelephone());

        return HttpResponse.status(HttpStatus.NO_CONTENT);
    }
}
