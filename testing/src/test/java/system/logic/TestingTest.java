package system.logic;

import com.epam.testing.system.logic.Testing;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class TestingTest {
    Testing testing = new Testing();

    @Test
    void getTestsByTutorTest() {
        JSONObject request = new JSONObject();
        request.put("login", "SomeLogin2");
        JSONObject response = testing.getTests(request);
        System.out.println(response.getOrDefault("tests", "no").toString());
        Assert.assertEquals("success", response.get("status").toString());
    }

    @Test
    void getTestsTest() {
        JSONObject response = testing.getTests();
        System.out.println(response.getOrDefault("tests", "no").toString());
        Assert.assertEquals("success", response.get("status").toString());
    }

    @Test
    void getTestByIdTest() {
        JSONObject request = new JSONObject();
        request.put("id", 1);
        JSONObject response = testing.getTestById(request);
        System.out.println(response.getOrDefault("test", "no").toString());
        Assert.assertEquals("success", response.get("status"));
    }
}
