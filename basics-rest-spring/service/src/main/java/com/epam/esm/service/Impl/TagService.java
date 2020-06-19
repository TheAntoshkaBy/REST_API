package com.epam.esm.service.Impl;

import com.epam.esm.entity.Tag;

import java.util.List;

public interface TagService {
    List<Tag> findAll();

    Tag find(int id);

    void delete(int id);

    void create(Tag tag);
}
