package com.epam.esm.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.Table;
import lombok.Data;

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
