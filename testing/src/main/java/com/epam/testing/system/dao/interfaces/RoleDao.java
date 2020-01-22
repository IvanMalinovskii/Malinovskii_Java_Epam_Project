package com.epam.testing.system.dao.interfaces;

import com.epam.testing.system.entities.Role;

public interface RoleDao {
    int getRoleId(Role.UserRole role);
}
