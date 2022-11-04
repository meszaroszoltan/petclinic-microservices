package org.quarkusframework.samples.petclinic.vets;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Set;

import static javax.transaction.Transactional.TxType.REQUIRED;

@ApplicationScoped
class VetInit {
    @Inject
    EntityManager em;

    @Transactional(REQUIRED)
    void init() {
        Specialty radiology = new Specialty("radiology");
        em.persist(radiology);
        Specialty surgery = new Specialty("surgery");
        em.persist(surgery);
        Specialty dentistry = new Specialty("dentistry");
        em.persist(dentistry);

        var vet1 = new Vet("James", "Carter", Collections.emptySet());
        em.persist(vet1);
        var vet2 = new Vet("Helen", "Leary", Set.of(radiology));
        em.persist(vet2);
        var vet3 = new Vet("Linda", "Douglas", Set.of(surgery, dentistry));
        em.persist(vet3);
        var vet4 = new Vet("Rafael", "Ortega", Set.of(radiology));
        em.persist(vet4);
        var vet5 = new Vet("Henry", "Stevens", Set.of(surgery));
        em.persist(vet5);
        var vet6 = new Vet("Sharon", "Jenkins", Set.of(radiology));
        em.persist(vet6);
    }

}
