package system.dao.jdbc;

import com.epam.testing.system.dao.interfaces.QuestionDao;
import com.epam.testing.system.dao.interfaces.TestDao;
import com.epam.testing.system.dao.jdbc.JdbcQuestionDao;
import com.epam.testing.system.dao.jdbc.JdbcTestDao;
import com.epam.testing.system.entities.Question;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.List;

public class JdbcQuestionDaoTest {
    QuestionDao questionDao = new JdbcQuestionDao();
    TestDao testDao = new JdbcTestDao();

    @Test
    void insertQuestionTest() {
        Question question = new Question();
        question.setText("question");
        int testId = testDao.getTests().get(0).getId();
        question.setId(questionDao.insertQuestion(question, testId));
        List<Question> questions = questionDao.getQuestions(testId);
        int realIndex = questions.get(questions.size() - 1).getId();
        Assert.assertEquals(question.getId(), realIndex);
    }

    @Test
    void updateQuestionTest() {
        Question question = new Question();
        question.setText("question_updated");
        int testId = testDao.getTests().get(0).getId();
        List<Question> questions = questionDao.getQuestions(testId);
        question.setId(questions.get(questions.size() - 1).getId());
        questionDao.updateQuestion(question);
        questions = questionDao.getQuestions(testId);
        String realText = questions.get(questions.size() - 1).getText();
        Assert.assertEquals(realText, question.getText());
    }

    @Test
    void deleteQuestionTest() {
        int testId = testDao.getTests().get(0).getId();
        List<Question> questions = questionDao.getQuestions(testId);
        int questionId = questions.get(questions.size() - 1).getId();
        questionDao.deleteQuestion(questionId);
        List<Question> questionsNew = questionDao.getQuestions(testId);
        Assert.assertEquals(questions.size() - 1, questionsNew.size());
    }
}
