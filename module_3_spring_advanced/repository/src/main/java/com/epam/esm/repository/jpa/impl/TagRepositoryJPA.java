package com.epam.esm.repository.jpa.impl;

import com.epam.esm.constant.SQLRequests;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.RepositoryNotFoundException;
import com.epam.esm.exception.constant.ErrorTextMessageConstants;
import com.epam.esm.exception.entity.InvalidDataOutputMessage;
import com.epam.esm.repository.jpa.ShopJPARepository;
import com.epam.esm.repository.jpa.TagRepository;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;

@Repository
public class TagRepositoryJPA extends ShopJPARepository<Tag> implements TagRepository {

    @Override
    public void delete(long id) {
        int col = entityManager.createQuery(SQLRequests.DELETE_TAG_BY_ID)
            .setParameter(1, id).executeUpdate();
        if (col == 0) {
            throw new RepositoryNotFoundException(
                new InvalidDataOutputMessage(ErrorTextMessageConstants.NOT_FOUND_CERTIFICATE));
        }
    }

    @Override
    public Tag findByName(String name) {
        Tag tag;

        try {
            tag = (Tag) entityManager.createQuery(SQLRequests.FIND_TAG_BY_NAME)
                .setParameter(1, name).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

        return tag;
    }

    @Override
    public Tag findById(long id) {
        Tag tag = entityManager.find(Tag.class, id);

        if (tag == null) {
            throw new RepositoryNotFoundException(
                new InvalidDataOutputMessage(ErrorTextMessageConstants.NOT_FOUND_TAG));
        }

        return tag;
    }

    @SuppressWarnings("unchecked")
    public Tag findMostWidelyUsedTag() {
        String functionName = "greatest_tag";

        StoredProcedureQuery findByName = entityManager
            .createNamedStoredProcedureQuery(functionName);
        Object[] o = (Object[]) findByName.getSingleResult();
        BigInteger id = (BigInteger) o[0];
        Tag tag = new Tag(id.longValue(),(String) o[1]);

        return tag;
    }

    @Override
    public int getTagCount() {
        Long count = (Long) entityManager
            .createQuery(SQLRequests.FIND_COUNT_OF_TAG)
            .getSingleResult();

        return count.intValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Tag> findAll(int offset, int limit) {
        return entityManager.createQuery(SQLRequests.FIND_ALL_TAGS)
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
    }
}
