package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity(name = "certificate")
@Table(name = "certificate")
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "findByNameProcedure",
                procedureName = "return_t_certificate3",
                resultClasses = {Certificate.class},
                parameters = {
                        @StoredProcedureParameter
                                (
                                        name = "text",
                                        type = String.class
                                )
                })
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Certificate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certificate_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "duration_days")
    private Integer durationDays;

    @Column(name = "date_of_creation")
    private Date creationDate;

    @Column(name = "date_of_modification")
    private Date modification;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "c_t",
            joinColumns = @JoinColumn(name = "certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    public Certificate(Long id, String name, String description, Double price,
                       Date creationDate, Date modification, Integer durationDays) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.creationDate = creationDate;
        this.modification = modification;
        this.durationDays = durationDays;
    }

    public Certificate(String name, String description, Double price,
                       Integer durationDays) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.durationDays = durationDays;
    }

    public Certificate(Long id, String name, String description, Double price,
                       Date creationDate, Date modification, Integer durationDays, List<Tag> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.creationDate = creationDate;
        this.modification = modification;
        this.durationDays = durationDays;
    }
}
