package com.epam.esm.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "tag")
@Table(name = "tag")
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery
                (
                        name = "greatest_tag",
                        procedureName = "greater"
                )
})
@Data
public class Tag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Column(name = "name")
    private String name;

    public Tag() {
    }

    public Tag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
