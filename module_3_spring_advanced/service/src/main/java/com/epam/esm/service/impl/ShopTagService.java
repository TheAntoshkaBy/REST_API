package com.epam.esm.service.impl;

import com.epam.esm.pojo.TagPOJO;
import com.epam.esm.exception.tag.TagNotFoundException;
import com.epam.esm.repository.jpa.impl.TagRepositoryJPA;
import com.epam.esm.service.TagService;
import com.epam.esm.service.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopTagService implements TagService {
    private final TagRepositoryJPA tagRepository;
    private TagValidator tagValidator;

    @Autowired
    public ShopTagService(TagRepositoryJPA tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<TagPOJO> findAll() {
        return tagRepository.findAll()
                .stream()
                .map(TagPOJO::new)
                .collect(Collectors.toList());
    }

    @Override
    public TagPOJO find(long id) throws TagNotFoundException {
        return new TagPOJO(tagRepository.findById(id));
    }

    @Override
    public void delete(long id) throws TagNotFoundException {
        tagRepository.delete(id);
    }

    @Override
    public TagPOJO create(TagPOJO tag) {
        tagValidator.isCorrectTag(tag);
        return new TagPOJO(tagRepository.create(tag.pojoToEntity()));
    }

    @Override
    @Autowired
    public void setTagValidator(TagValidator tagValidator) {
        this.tagValidator = tagValidator;
    }
}
