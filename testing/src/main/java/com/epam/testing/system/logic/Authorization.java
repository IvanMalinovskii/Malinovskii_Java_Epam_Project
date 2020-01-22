package com.epam.testing.system.logic;

import com.epam.testing.system.dao.factories.DaoFactory;
import com.epam.testing.system.dao.factories.Source;
import com.epam.testing.system.dao.factories.StaticFactory;
import com.epam.testing.system.dao.interfaces.RoleDao;
import com.epam.testing.system.dao.interfaces.UserDao;
import com.epam.testing.system.entities.Role;
import com.epam.testing.system.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class Authorization {
    private static final Logger LOGGER = LogManager.getLogger(Authorization.class);
    private UserDao userDao;
    private RoleDao roleDao;

    public Authorization() {
        try {
            DaoFactory factory = StaticFactory.getFactory(Source.JDBC);
            userDao = factory.getUserDao();
            roleDao = factory.getRoleDao();
        } catch (Exception e) {
            LOGGER.error("getting factory error: " + e);
        }
    }

    public JSONObject doSignUp(JSONObject request) {
        JSONObject response = new JSONObject();
        User user = getFromJson(request);
        if (userDao.checkLogin(user.getLogin())) {
            response.put("status", "error");
            response.put("cause", "login");
        }
        else if (userDao.checkMail(user.getMail())) {
            response.put("status", "error");
            response.put("cause", "mail");
        }
        else {
            int roleId = roleDao.getRoleId(user.getRole().getRoles());
            if (roleId != -1) {
                user.getRole().setId(roleId);
                int insertedId = userDao.insertUser(user);
                if (insertedId != -1) {
                    response.put("status", "success");
                    response.put("id", insertedId);
                }
            }
            else {
                response.put("status", "error");
                response.put("cause", "data_base");
            }
        }
        return response;
    }

    public JSONObject doSignIn(JSONObject request) {
        JSONObject response = new JSONObject();
        User user = getFromJson(request);
        User gottenUser = userDao.getUser(user.getLogin(), user.getPassword());
        if (gottenUser != null) {
            response.put("status", "success");
            response.put("id", gottenUser.getId());
            response.put("name", gottenUser.getName());
            response.put("surname", gottenUser.getSurname());
            response.put("role", gottenUser.getRole().getRoles().toString().toLowerCase());
        }
        else {
            response.put("status", "error");
            response.put("cause", "data_base");
        }
        return response;
    }

    private User getFromJson(JSONObject jsonObject) {
        User user = new User();
        user.setLogin(jsonObject.getOrDefault("login", "none").toString());
        user.setPassword(jsonObject.getOrDefault("password", "none").toString());
        user.setMail(jsonObject.getOrDefault("mail", "none").toString());
        user.setName(jsonObject.getOrDefault("name", "none").toString());
        user.setSecondName(jsonObject.getOrDefault("secondName", "none").toString());
        user.setSurname(jsonObject.getOrDefault("surname", "none").toString());
        Role role = new Role();
        role.setRoles(Role.UserRole.valueOf(jsonObject.getOrDefault("role", "none").toString().toUpperCase()));
        user.setRole(role);
        return user;
    }
}
