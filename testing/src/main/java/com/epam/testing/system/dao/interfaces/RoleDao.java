package com.epam.testing.system.dao.interfaces;

import com.epam.testing.system.entities.Role;

/**
 * describes role dao
 */
public interface RoleDao {
    int getRoleId(Role.UserRole role);
}
