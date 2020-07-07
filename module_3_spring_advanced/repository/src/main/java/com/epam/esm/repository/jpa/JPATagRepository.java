package com.epam.esm.repository.jpa;

import com.epam.esm.entity.Tag;

import java.util.List;

public interface JPATagRepository {
    Tag findById(long id);

    List<Tag> findAll();

    Tag create(Tag tag);

    void delete(long id);
}
