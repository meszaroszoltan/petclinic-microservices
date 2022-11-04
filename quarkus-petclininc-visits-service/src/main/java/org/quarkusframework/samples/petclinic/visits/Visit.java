package org.quarkusframework.samples.petclinic.visits;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "visits")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "visit_date")
    private String date;

    @Size(max = 8192)
    @Column(name = "description")
    private String description;

    @Column(name = "pet_id")
    private int petId;

    public Integer getId() {
        return id;
    }

    public String  getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(final int petId) {
        this.petId = petId;
    }

    public Visit(Integer id, String date, String description, int petId) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.petId = petId;
    }

    public Visit() {
    }
}
