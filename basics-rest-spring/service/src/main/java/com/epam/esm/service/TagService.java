package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.tag.TagNotFoundException;
import com.epam.esm.service.validator.TagValidator;

import java.util.List;

/**
 * @author Anton Vedenichev (https://github.com/TheAntoshkaBy)
 */
public interface TagService {

    /**
     * This method finds all tags from database using DAO
     **/
    List<Tag> findAll();

    /**
     * This method finds concrete Tag By Id using DAO
     *
     * @param id Tag Id
     * @return Tag
     **/
    Tag find(int id) throws TagNotFoundException;

    /**
     * This method delete tag by transmitted tag id
     * to certificate with transmitted certificate id using DAO
     *
     * @param id certificate id which will be edit
     **/
    void delete(int id) throws TagNotFoundException;

    /**
     * This method add new Tag
     *
     * @param tag Tag object
     **/
    void create(Tag tag);

    void setTagValidator(TagValidator tagValidator);
}
