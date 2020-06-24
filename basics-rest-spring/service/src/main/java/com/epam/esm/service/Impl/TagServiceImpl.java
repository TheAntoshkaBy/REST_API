package com.epam.esm.service.Impl;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.service.TagService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagServiceImpl implements TagService {

    private final TagDAO TagDAO;

    public TagServiceImpl(TagDAO TagDAO) {
        this.TagDAO = TagDAO;
    }

    @Override
    public List<Tag> findAll() {
        return TagDAO.findAll();
    }

    @Override
    public Tag find(int id) throws TagNotFoundException {
        return TagDAO.findTagById(id);
    }

    @Override
    public void delete(int id) throws TagNotFoundException {
        TagDAO.deleteTagById(id);
    }

    @Override
    public void create(Tag tag) {
        TagDAO.addTag(tag);
    }
}