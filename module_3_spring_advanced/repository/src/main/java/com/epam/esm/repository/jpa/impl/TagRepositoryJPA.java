package com.epam.esm.repository.jpa.impl;

import com.epam.esm.constant.SQLRequests;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.constant.ErrorTextMessageConstants;
import com.epam.esm.exception.entity.InvalidDataOutputMessage;
import com.epam.esm.repository.jpa.ShopJPARepository;
import com.epam.esm.repository.jpa.TagRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.StoredProcedureQuery;
import java.math.BigInteger;
import java.util.List;

@Transactional
@Repository
public class TagRepositoryJPA extends ShopJPARepository<Tag> implements TagRepository {

    @Override
    public void delete(long id) {
        int col = entityManager.createQuery(SQLRequests.DELETE_TAG_BY_ID)
                .setParameter(1, id).executeUpdate();
        if(col == 0){
            throw new RepositoryException(new InvalidDataOutputMessage("Tag",
                    ErrorTextMessageConstants.NOT_FOUND_CERTIFICATE));
        }
    }

    @Override
    public Tag findById(long id) {
        Tag tag = entityManager.find(Tag.class, id);
        if(tag == null){
            throw new RepositoryException(new InvalidDataOutputMessage("Tag",
                    ErrorTextMessageConstants.NOT_FOUND_TAG));
        }
        return tag;
    }

    @SuppressWarnings("unchecked")
    public Long findMostWidelyUsedTag() {
        StoredProcedureQuery findByName = entityManager
                .createNamedStoredProcedureQuery("module3");
        BigInteger o = (BigInteger) findByName.getSingleResult();
        return o.longValue();
    }

    @Override
    public int getTagCount() {
        Long count = (Long) entityManager.createQuery(SQLRequests.FIND_COUNT_OF_TAG).getSingleResult();
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
