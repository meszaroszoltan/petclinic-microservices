package org.quarkusframework.samples.petclinic.vets;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;;

@Path("/vets")
public class VetResource {

    @Inject
    EntityManager em;

    @Inject
    VetInit vetInit;

    @PostConstruct
    void postConstruct() {
        vetInit.init();
    }

    @GET
    public List<Vet> showResourcesVetList() {
        return em.createQuery("from Vet", Vet.class).getResultList();
    }
}
