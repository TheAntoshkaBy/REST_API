package com.epam.esm.repository.jpa.impl;

import com.epam.esm.constant.ErrorTextMessageConstants;
import com.epam.esm.constant.SQLRequests;
import com.epam.esm.entity.InvalidDataMessage;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.certificate.CertificateNotFoundException;
import com.epam.esm.exception.tag.TagNotFoundException;
import com.epam.esm.repository.jpa.JPATagRepository;
import com.epam.esm.repository.jpa.ShopJPARepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class TagJPAJPQLRepository extends ShopJPARepository<Tag> implements JPATagRepository {
    @Override
    public Tag create(Tag tag) {
        entityManager.persist(tag);
        return tag;
    }

    @Override
    public void delete(long id) {
        if (entityManager.createQuery(SQLRequests.DELETE_TAG_BY_ID)
                .setParameter(1, id).executeUpdate() == 0) {
            throw new CertificateNotFoundException(new InvalidDataMessage(
                    ErrorTextMessageConstants.NOT_FOUND_TAG
            ));
        }
    }

    @Override
    public Tag findById(long id) {
        Tag tag = entityManager.find(Tag.class, id);
        if (tag == null) {
            throw new TagNotFoundException(
                    new InvalidDataMessage(ErrorTextMessageConstants.NOT_FOUND_TAG
                    ));
        }
        return tag;
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<Tag> findAll() {
        return entityManager.createQuery(SQLRequests.FIND_ALL_TAGS).getResultList();
    }
}
