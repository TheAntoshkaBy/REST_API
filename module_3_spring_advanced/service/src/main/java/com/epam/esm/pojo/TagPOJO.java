package com.epam.esm.pojo;

import com.epam.esm.entity.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class TagPOJO {
    private Long id;
    private String name;

    public TagPOJO(Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
    }

    public TagPOJO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
