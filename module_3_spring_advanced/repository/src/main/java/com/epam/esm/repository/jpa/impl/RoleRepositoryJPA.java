package com.epam.esm.repository.jpa.impl;

import com.epam.esm.constant.SQLRequests;
import com.epam.esm.entity.Role;
import com.epam.esm.repository.jpa.RoleRepository;
import com.epam.esm.repository.jpa.ShopJPARepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
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

    }

    @Override
    public Role findById(long id) {
        return null;
    }

    @Override
    public List<Role> findAll(int offset, int limit) {
        return null;
    }
}
