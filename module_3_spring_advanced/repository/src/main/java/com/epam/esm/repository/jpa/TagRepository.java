package com.epam.esm.repository.jpa;

import com.epam.esm.entity.Tag;

import java.util.List;

public interface TagRepository {
    Tag findById(long id);

    List<Tag> findAll(int limit, int offset);

    Tag create(Tag tag);

    void delete(long id);

    int getTagCount();

    Long findMostWidelyUsedTag();
}
