package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDTO {
    private Long id;
    private String name;

    public TagDTO() {
    }

    public TagDTO(Long id, String name) {
        this.id = id;
        this.name = name;
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
        TagDTO tag = (TagDTO) o;
        return Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public static TagPOJO dtoToPOJO(TagDTO tag){
        return new TagPOJO(tag.id,tag.name);
    }
    public static TagDTO pojoToDTO(TagPOJO tag){
        return new TagDTO(tag.getId(),tag.getName());
    }
}
