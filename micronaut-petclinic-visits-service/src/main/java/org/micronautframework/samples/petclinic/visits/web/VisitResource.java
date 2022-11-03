package org.micronautframework.samples.petclinic.visits.web;


import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/")
public class VisitResource {

    @Get(produces = MediaType.TEXT_PLAIN) //
    public String index() {
        return "Hello World"; //
    }
}
