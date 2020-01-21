package system.dao.jdbc;

import com.epam.testing.system.dao.jdbc.JdbcTestDao;
import com.epam.testing.system.entities.Answer;
import com.epam.testing.system.entities.Question;
import com.epam.testing.system.entities.Subject;
import com.epam.testing.system.entities.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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

    @Test
    void insertTestTest() {
        com.epam.testing.system.entities.Test test = new com.epam.testing.system.entities.Test();
        test.setTitle("third");
        Subject subject = new Subject();
        subject.setId(1);
        test.setSubject(subject);
        User user = new User();
        user.setId(2);
        test.setUser(user);
        List<Question> questions = new ArrayList<>();
        Question question = new Question();
        question.setText("quest1");
        List<Answer> answers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Answer answer = new Answer();
            answer.setText("answ" + i);
            answer.setRight(i == 0);
            answers.add(answer);
        }
        question.setAnswers(answers);
        questions.add(question);
        test.setQuestions(questions);
        int id = testDao.insertTest(test);

        Assert.assertEquals(10, id);
    }

    @Test
    void updateTestTest() {
        com.epam.testing.system.entities.Test test = new com.epam.testing.system.entities.Test();
        test.setId(9);
        test.setTitle("newTitle");
        Subject subject = new Subject();
        subject.setId(2);
        test.setSubject(subject);
        User user = new User();
        user.setId(2);
        test.setUser(user);

        testDao.updateTest(test);

        Assert.assertEquals("newTitle", testDao.getTestById(9).getTitle());
    }

    @Test
    void deleteTestTest() {
        int initialSize = testDao.getTests().size();
        int expectedSize = initialSize - 1;
        testDao.deleteTest(9);

        Assert.assertEquals(expectedSize, testDao.getTests().size());
    }
}
