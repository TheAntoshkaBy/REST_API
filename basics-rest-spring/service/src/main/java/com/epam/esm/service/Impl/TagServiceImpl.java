package com.epam.esm.service.Impl;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.tag.TagNotFoundException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagServiceImpl implements TagService {

    private final TagDAO TagDAO;
    private TagValidator tagValidator;

    public TagServiceImpl(TagDAO TagDAO) {
        this.TagDAO = TagDAO;
    }

    @Autowired
    public void setTagValidator(TagValidator tagValidator) {
        this.tagValidator = tagValidator;
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
        tagValidator.isCorrectTag(tag);
        TagDAO.addTag(tag);
    }
}
