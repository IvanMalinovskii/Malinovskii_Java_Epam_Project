package system.dao.jdbc;

import com.epam.testing.system.dao.interfaces.AnswerDao;
import com.epam.testing.system.dao.interfaces.QuestionDao;
import com.epam.testing.system.dao.interfaces.TestDao;
import com.epam.testing.system.dao.jdbc.JdbcAnswerDao;
import com.epam.testing.system.dao.jdbc.JdbcQuestionDao;
import com.epam.testing.system.dao.jdbc.JdbcTestDao;
import com.epam.testing.system.entities.Answer;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.List;

class JdbcAnswerDaoTest {
    private TestDao testDao = new JdbcTestDao();
    private QuestionDao questionDao = new JdbcQuestionDao();
    private AnswerDao answerDao = new JdbcAnswerDao();

    @Test
    void insertAnswerTest() {
        Answer answer = new Answer();
        answer.setText("answer_test");
        answer.setRight(true);
        int questionId = questionDao.getQuestions(testDao.getTests().get(0).getId()).get(0).getId();
        int answerId = answerDao.insertAnswer(answer, questionId);
        List<Answer> answers = answerDao.getAnswers(questionId);
        Assert.assertEquals(answerId, answers.get(answers.size() - 1).getId());
    }

    @Test
    void updateAnswerTest() {
        Answer answer = new Answer();
        int questionId = questionDao.getQuestions(testDao.getTests().get(0).getId()).get(0).getId();
        List<Answer> answers = answerDao.getAnswers(questionId);
        int answerId = answers.get(answers.size() - 1).getId();
        answer.setId(answerId);
        answer.setText("answer_test_updated");
        answer.setRight(true);
        answerDao.updateAnswer(answer);
        List<Answer> answersNew = answerDao.getAnswers(questionId);
        Assert.assertEquals(answer.getText(), answersNew.get(answersNew.size() - 1).getText());
    }

    @Test
    void deleteTest() {
        int questionId = questionDao.getQuestions(testDao.getTests().get(0).getId()).get(0).getId();
        List<Answer> answers = answerDao.getAnswers(questionId);
        int answerId = answers.get(answers.size() - 1).getId();
        answerDao.deleteAnswer(answerId);
        List<Answer> answersNew = answerDao.getAnswers(questionId);
        Assert.assertEquals(answers.size() - 1, answersNew.size());
    }
}
