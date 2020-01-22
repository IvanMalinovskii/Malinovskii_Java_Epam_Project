package system.logic;

import com.epam.testing.system.entities.Role;
import com.epam.testing.system.logic.Authorization;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class AuthorizationTest {
    Authorization authorization = new Authorization();

    @Test
    void doSignUpTest() {
        JSONObject userObject = new JSONObject();
        userObject.put("login", "testLog");
        userObject.put("password", "67876566fF");
        userObject.put("mail", "newMail@mail.ru");
        userObject.put("name", "name");
        userObject.put("secondName", "secondName");
        userObject.put("surname", "surname");
        userObject.put("role", Role.UserRole.STUDENT.toString().toLowerCase());
        JSONObject response = authorization.doSignUp(userObject);
        System.out.println(response.getOrDefault("cause", "no"));
        Assert.assertEquals("success", response.get("status").toString());
    }

    @Test
    void doSignIn() {
        JSONObject userObject = new JSONObject();
        userObject.put("login", "testLog");
        userObject.put("password", "67876566fF");
        JSONObject response = authorization.doSignIn(userObject);
        System.out.println(response.getOrDefault("cause", "no"));
        Assert.assertEquals("success", response.get("status").toString());
        Assert.assertEquals("name", response.get("name"));
    }


}
