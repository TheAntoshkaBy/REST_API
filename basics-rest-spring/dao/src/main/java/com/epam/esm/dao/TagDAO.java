package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.List;

public interface TagDAO {
    List<Tag> getAll();

    Tag getTagById(int id);

    int addTag(Tag tag);

    void deleteTagById(int id);
}
