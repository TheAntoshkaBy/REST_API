package com.epam.esm.service.impl;

import com.epam.esm.pojo.TagPOJO;
import com.epam.esm.repository.jpa.impl.TagRepositoryJPA;
import com.epam.esm.service.TagService;
import com.epam.esm.service.support.ServiceSupporter;
import com.epam.esm.service.validator.TagValidator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ShopTagService implements TagService {

    private TagRepositoryJPA tagRepository;
    private TagValidator tagValidator;

    @Autowired
    public ShopTagService(TagRepositoryJPA tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<TagPOJO> findAll(int page, int size) {
        page = ServiceSupporter.convertPaginationPageToDbOffsetParameter(page, size);

        return ServiceSupporter.convertTagEntityToTagPOJO(tagRepository
            .findAll(--page, size));
    }

    @Override
    public TagPOJO find(long id) {
        return new TagPOJO(tagRepository.findById(id));
    }

    @Override
    public TagPOJO findMostWidelyUsedTag() {
        return new TagPOJO(tagRepository.findMostWidelyUsedTag());
    }

    @Override
    public void delete(long id) {
        tagRepository.delete(id);
    }

    @Override
    public TagPOJO create(TagPOJO tag) {
        tagValidator.isCorrectTag(tag);
        return new TagPOJO(tagRepository.create(ServiceSupporter.convertTagPojoToTag(tag)));
    }

    @Override
    public int getTagCount() {
        return tagRepository.getTagCount();
    }

    @Override
    @Autowired
    public void setTagValidator(TagValidator tagValidator) {
        this.tagValidator = tagValidator;
    }
}
