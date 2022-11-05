package org.quarkusframework.samples.petclinic.customers.web;

import org.quarkusframework.samples.petclinic.customers.models.Owner;
import org.quarkusframework.samples.petclinic.customers.models.Pet;
import org.quarkusframework.samples.petclinic.customers.models.PetType;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Collections;

import static javax.transaction.Transactional.TxType.REQUIRED;

@ApplicationScoped
class Init {
    @Inject
    EntityManager em;

    volatile boolean alreadyInitized;

    @Transactional(REQUIRED)
    synchronized void init() {
        if (!alreadyInitized) {
            var owner = new Owner(null, "first", "last", "addr", "city", "123", Collections.emptySet());
            var type = new PetType(null, "cat");
            var pet = new Pet(null, "name", "new Date()", type, owner);

            em.persist(owner);
            em.persist(type);
            em.persist(pet);
            alreadyInitized = true;
        }
    }

}
