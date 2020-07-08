package com.epam.esm.service.impl;

import com.epam.esm.entity.CertificatePOJO;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagPOJO;
import com.epam.esm.exception.tag.TagNotFoundException;
import com.epam.esm.repository.jpa.impl.TagJPAJPQLRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.service.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    private TagJPAJPQLRepository tagRepository;
    private TagValidator tagValidator;

    @Autowired
    public TagServiceImpl(TagJPAJPQLRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<TagPOJO> findAll() {
        return tagRepository.findAll()
                .stream()
                .map(TagPOJO::entityToPOJO)
                .collect(Collectors.toList());
    }

    @Override
    public TagPOJO find(long id) throws TagNotFoundException {
        return TagPOJO.entityToPOJO(tagRepository.findById(id));
    }

    @Override
    public void delete(long id) throws TagNotFoundException {
        tagRepository.delete(id);
    }

    @Override
    public TagPOJO create(TagPOJO tag) {
        tagValidator.isCorrectTag(tag);
        return TagPOJO.entityToPOJO(tagRepository.create(TagPOJO.pojoToEntity(tag)));
    }

    @Override
    @Autowired
    public void setTagValidator(TagValidator tagValidator) {
        this.tagValidator = tagValidator;
    }
}
