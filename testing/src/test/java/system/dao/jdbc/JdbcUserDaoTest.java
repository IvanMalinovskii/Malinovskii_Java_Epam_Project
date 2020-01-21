package system.dao.jdbc;

import com.epam.testing.system.dao.interfaces.UserDao;
import com.epam.testing.system.dao.jdbc.JdbcUserDao;
import com.epam.testing.system.entities.Role;
import com.epam.testing.system.entities.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

class JdbcUserDaoTest {
    private UserDao userDao = new JdbcUserDao();

    @Test
    void getUserTest() {
        User user = userDao.getUser("SomeLogin", "67876566fF");

        Assert.assertEquals(1, user.getId());
    }

    @Test
    void checkLoginTest() {
        Assert.assertTrue(userDao.checkLogin("SomeLogin"));
        Assert.assertFalse(userDao.checkLogin("SL"));
    }

    @Test
    void checkMailTest() {
        Assert.assertTrue(userDao.checkMail("someMail@list.ru"));
        Assert.assertFalse(userDao.checkMail("mail"));
    }

    @Test
    void insertUserTest() {
        User user = new User();
        user.setLogin("LIN12");
        user.setPassword("pass1234");
        user.setMail("maillll@mail.ru");
        user.setName("name");
        user.setSecondName("second");
        user.setSurname("surname");
        Role role = new Role();
        role.setId(1);
        user.setRole(role);
        Assert.assertFalse(userDao.checkLogin(user.getLogin()));
        int id = userDao.insertUser(user);
        Assert.assertTrue(userDao.checkLogin(user.getLogin()));
    }
}
