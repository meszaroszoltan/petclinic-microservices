package org.micronautframework.samples.petclinic.customers.web;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Size;
import java.util.Date;

public class PetRequest {
    private int id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @Size(min = 1)
    private String name;

    private int typeId;

    public PetRequest(int id, Date birthDate, String name, int typeId) {
        this.id = id;
        this.birthDate = birthDate;
        this.name = name;
        this.typeId = typeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}
