/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.quarkusframework.samples.petclinic.visits;


import io.quarkus.runtime.Startup;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;

@Startup
@Path("/")
@Transactional
public class VisitResource {

    public VisitResource() {
    }

    @Inject
    EntityManager em;

    @POST
    @Path("owners/{ownerId}/pets/{petId}/visits")
    public Response create(
        @Valid Visit visit,
        @PathParam("ownerId") int ownerId,
        @PathParam("petId") int petId
    ) {
        visit.setPetId(petId);
        em.persist(visit);

        return Response.status(201).entity(visit).build();
    }

    @GET
    @Path("owners/{ownerId}/pets/{petId}/visits")
    public List<Visit> visits(
        @PathParam("ownerId") int ownerId,
        @PathParam("petId") int petId
    ) {
        return em
            .createQuery("select v from Visit v where v.petId = :petId", Visit.class)
            .setParameter("petId", petId)
            .getResultList();
    }

    @GET
    @Path("pets/visits")
    public Visits visitsMultiGet(@QueryParam("petId") List<Integer> petIds) {
        final List<Visit> byPetIdIn = em
            .createQuery("select v from Visit v where v.petId in :petIds", Visit.class)
            .setParameter("petIds", petIds)
            .getResultList();
        return new Visits(byPetIdIn);
    }

    public static class Visits {
        private final List<Visit> items;

        Visits(List<Visit> items) {
            this.items = items;
        }

        public List<Visit> getItems() {
            return items;
        }
    }
}
