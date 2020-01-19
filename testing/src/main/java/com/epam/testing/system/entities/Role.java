package com.epam.testing.system.entities;

/**
 * describes a role
 */
public class Role {
    public enum UserRole {
        NONE,
        STUDENT,
        TUTOR
    }

    private int id;
    private UserRole roles;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserRole getRoles() {
        return roles;
    }

    public void setRoles(UserRole roles) {
        this.roles = roles;
    }
}
