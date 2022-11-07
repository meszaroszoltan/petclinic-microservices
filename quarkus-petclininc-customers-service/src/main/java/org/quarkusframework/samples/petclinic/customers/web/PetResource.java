package org.quarkusframework.samples.petclinic.customers.web;


import io.quarkus.runtime.Startup;
import org.quarkusframework.samples.petclinic.customers.models.PetType;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@Startup
@Path("")
@Transactional
public class PetResource {
    @Inject
    EntityManager em;

    @Inject
    Init init;

    @PostConstruct
    void init() {
        init.init();
    }

    public PetResource() {

    }

    @GET
    @Path("/petTypes")
    public List<PetType> getPetTypes() {
        return em
            .createQuery("select p from PetType p", PetType.class)
            .getResultList();
    }

    // /owners stuff moved
}
