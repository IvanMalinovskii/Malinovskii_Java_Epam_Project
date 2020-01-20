package system.dao.jdbc;

import com.epam.testing.system.dao.JdbcTestDao;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.List;

class JdbcTestDaoTest {

    private JdbcTestDao testDao = new JdbcTestDao();

    @Test
    void getAllTestsTest() {
        List<com.epam.testing.system.entities.Test> tests = testDao.getTests();

        Assert.assertEquals(1, tests.get(0).getId());
    }

    @Test
    void getTestsByTutorTest() {
        List<com.epam.testing.system.entities.Test> tests = testDao.getTestsByTutor("SomeLogin2");

        Assert.assertEquals("Igorni", tests.get(0).getUser().getSurname());
    }

    @Test
    void getTestById() {
        com.epam.testing.system.entities.Test test = testDao.getTestById(1);
        System.out.println(test);

        Assert.assertEquals(2, test.getQuestions().size());
    }
}
