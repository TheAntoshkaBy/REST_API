package com.epam.esm.pojo;

import com.epam.esm.entity.Tag;

import java.util.Objects;

public class TagPOJO {
    private Long id;
    private String name;

    public TagPOJO() {
    }

    public TagPOJO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public TagPOJO(Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagPOJO tag = (TagPOJO) o;
        return Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public Tag pojoToEntity() {
        return new Tag(this.id, this.name);
    }
}
