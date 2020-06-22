package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TagNotFoundException;

import java.util.List;

/**
 * @author Anton Vedenichev (https://github.com/TheAntoshkaBy)
 */
public interface TagDAO {

    /**
     * This method finds all tags from database
     **/
    List<Tag> findAll();

    /**
     * This method finds concrete Tag By Id
     *
     * @param id  Tag Id
     * @return Tag
     **/
    Tag findTagById(int id) throws TagNotFoundException;

    /**
     * This method add new Tag
     *
     * @param tag  Tag object
     **/
    int addTag(Tag tag);

    /**
     * This method delete tag by transmitted tag id to certificate with transmitted certificate id
     *
     * @param id  certificate id which will be edit
     **/
    void deleteTagById(int id) throws TagNotFoundException;

    /**
     * This method delete all tags
     **/
    void deleteAll() throws TagNotFoundException;
}
