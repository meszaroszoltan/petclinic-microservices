package org.quarkusframework.samples.petclinic.customers.web;

import io.quarkus.runtime.Startup;
import org.quarkusframework.samples.petclinic.customers.models.Owner;
import org.quarkusframework.samples.petclinic.customers.models.Pet;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Startup
@Path("/owners")
@Transactional
public class OwnerResource {

    @Inject
    EntityManager em;

    @Inject
    Init init;

    @PostConstruct
    void init() {
        init.init();
    }

    public OwnerResource() {
    }


    @POST
    public Response createOwner(@Valid Owner owner) {
        em.persist(owner);
        return Response.status(201).entity(owner).build();
    }

    @GET
    @Path("/{ownerId}")
    public Optional<Owner> findOwner(@PathParam("ownerId") int ownerId) {
        var owner = em
            .createQuery("select o from Owner o where o.id = :ownerId", Owner.class)
            .setParameter("ownerId", ownerId)
            .getSingleResult();
        return Optional.of(owner);
    }

    @GET
    public List<Owner> findAll() {
        return em
            .createQuery("select o from Owner o", Owner.class)
            .getResultList();
    }

    @PUT
    @Path("/{ownerId}")
    public Response updateOwner(
        @PathParam("ownerId") int ownerId,
        @Valid Owner ownerRequest
    ) {
        final Optional<Owner> owner = findOwner(ownerId);

        final Owner ownerModel = owner.orElseThrow(() -> new RuntimeException("Owner " + ownerId + " not found"));
        // This is done by hand for simplicity purpose. In a real life use-case we should consider using MapStruct.
        ownerModel.setFirstName(ownerRequest.getFirstName());
        ownerModel.setLastName(ownerRequest.getLastName());
        ownerModel.setCity(ownerRequest.getCity());
        ownerModel.setAddress(ownerRequest.getAddress());
        ownerModel.setTelephone(ownerRequest.getTelephone());
        em.persist(ownerModel);

        return Response.status(204).build();
    }


    @POST
    @Path("/{ownerId}/pets")
    public Response processCreationForm(
        PetRequest petRequest,
        @PathParam("ownerId") int ownerId
    ) {
        final Pet pet = new Pet();
        final Owner optionalOwner = em.createQuery("select o from Owner o where o.id=:ownerId", Owner.class)
            .setParameter("ownerId", ownerId)
            .getResultList()
            .get(0);
        if (optionalOwner == null) {
            throw new RuntimeException("Owner " + ownerId + " not found");
        }
        optionalOwner.addPet(pet);

        return Response.status(201).entity(pet).build();
    }

    @PUT
    @Path("/{ownerId}/pets/{petId}")
    public Response processUpdateForm(
        @PathParam("ownerId") int ownerId,
        @PathParam("petId") int petId,
        PetRequest petRequest
    ) {
        Pet pet = em
            .createQuery("select p from Pet p where p.id=:petId", Pet.class)
            .setParameter("petId", petId)
            .getSingleResult();
        pet.setName(petRequest.getName());
        pet.setBirthDate(petRequest.getBirthDate());
        em.persist(pet);
        return Response.status(204).build();
    }

    @GET
    @Path("/{ownerId}/pets/{petId}")
    public PetDetails findPet(
        @PathParam("ownerId") int ownerId,
        @PathParam("petId") int petId
    ) {
        Pet pet = em
            .createQuery("select p from Pet p where p.id=:petId", Pet.class)
            .setParameter("petId", petId)
            .getSingleResult();
        return new PetDetails(pet);
    }

}
