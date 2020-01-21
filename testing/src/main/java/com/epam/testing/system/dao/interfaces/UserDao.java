package com.epam.testing.system.dao.interfaces;

import com.epam.testing.system.entities.User;

public interface UserDao {
    User getUser(String login, String password);
    boolean checkLogin(String login);
    boolean checkMail(String mail);
    int insertUser(User user);
    boolean deleteUser(int userId);
}
