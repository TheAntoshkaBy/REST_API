package com.epam.esm.repository.jpa;

import com.epam.esm.entity.Role;

public interface RoleRepository {
    Role findByRoleName(String roleName);
}
