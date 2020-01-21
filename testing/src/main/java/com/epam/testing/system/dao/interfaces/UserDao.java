package com.epam.testing.system.dao.interfaces;

import com.epam.testing.system.entities.User;

public interface UserDao {
    User getUser(String login, String password);
    boolean checkLogin(String login);
    boolean checkEmail(String email);
    int insertUser(User user);
}
