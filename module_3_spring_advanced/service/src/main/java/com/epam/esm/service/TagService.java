package com.epam.esm.service;

import com.epam.esm.pojo.TagPOJO;
import com.epam.esm.service.validator.TagValidator;
import java.util.List;

/**
 * @author Anton Vedenichev (https://github.com/TheAntoshkaBy)
 */
public interface TagService {

    /**
     * This method finds all tags from database using DAO
     **/
    List<TagPOJO> findAll(int offset, int limit);

    int getTagCount();

    /**
     * This method finds concrete Tag By Id using DAO
     *
     * @param id Tag Id
     * @return Tag
     **/
    TagPOJO find(long id);

    TagPOJO findMostWidelyUsedTag();

    /**
     * This method delete tag by transmitted tag id to certificate with transmitted certificate id
     * using DAO
     *
     * @param id certificate id which will be edit
     **/
    void delete(long id);

    /**
     * This method add new Tag
     *
     * @param tag Tag object
     **/
    TagPOJO create(TagPOJO tag);

    void setTagValidator(TagValidator tagValidator);
}
