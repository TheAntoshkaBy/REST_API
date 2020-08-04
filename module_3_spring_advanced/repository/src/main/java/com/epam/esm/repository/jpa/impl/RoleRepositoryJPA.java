package com.epam.esm.repository.jpa.impl;

import com.epam.esm.constant.SQLRequests;
import com.epam.esm.entity.Role;
import com.epam.esm.exception.NotSupportedOperationException;
import com.epam.esm.exception.constant.EntityNameConstant;
import com.epam.esm.exception.constant.ErrorTextMessageConstants;
import com.epam.esm.exception.entity.InvalidDataOutputMessage;
import com.epam.esm.repository.jpa.RoleRepository;
import com.epam.esm.repository.jpa.ShopJPARepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepositoryJPA extends ShopJPARepository<Role> implements RoleRepository {

    @Override
    public Role findByRoleName(String roleName) {
        return (Role) entityManager
            .createQuery(SQLRequests.FIND_ROLE_BY_NAME)
            .setParameter(1, roleName)
            .getSingleResult();
    }

    @Override
    public void delete(long id) {
        throw new NotSupportedOperationException(
            new InvalidDataOutputMessage(
                EntityNameConstant.ROLE,
                ErrorTextMessageConstants.ROLE_NOT_SUPPORTED_OPERATION)
        );
    }

    @Override
    public Role findById(long id) {
        throw new NotSupportedOperationException(
            new InvalidDataOutputMessage(
                EntityNameConstant.ROLE,
                ErrorTextMessageConstants.ROLE_NOT_SUPPORTED_OPERATION)
        );
    }

    @Override
    public List<Role> findAll(int offset, int limit) {
        throw new NotSupportedOperationException(
            new InvalidDataOutputMessage(
                EntityNameConstant.ROLE,
                ErrorTextMessageConstants.ROLE_NOT_SUPPORTED_OPERATION)
        );
    }
}
