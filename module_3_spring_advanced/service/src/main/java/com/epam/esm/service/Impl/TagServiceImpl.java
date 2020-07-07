package com.epam.esm.service.Impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.tag.TagNotFoundException;
import com.epam.esm.repository.jpa.impl.TagJPAJPQLRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.service.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    private TagJPAJPQLRepository tagRepository;
    private TagValidator tagValidator;

    @Autowired
    public TagServiceImpl(TagJPAJPQLRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    @Override
    public Tag find(long id) throws TagNotFoundException {
        return tagRepository.findById(id);
    }

    @Override
    public void delete(long id) throws TagNotFoundException {
        tagRepository.delete(id);
    }

    @Override
    public Tag create(Tag tag) {
        tagValidator.isCorrectTag(tag);
        return tagRepository.create(tag);
    }

    @Override
    @Autowired
    public void setTagValidator(TagValidator tagValidator) {
        this.tagValidator = tagValidator;
    }
}
