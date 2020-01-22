package com.epam.testing.system.logic;

import com.epam.testing.system.dao.factories.DaoFactory;
import com.epam.testing.system.dao.factories.Source;
import com.epam.testing.system.dao.factories.StaticFactory;
import com.epam.testing.system.dao.interfaces.*;
import com.epam.testing.system.entities.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

public class Testing {
    private static final Logger LOGGER = LogManager.getLogger(Testing.class);
    private TestDao testDao;
    private QuestionDao questionDao;
    private AnswerDao answerDao;
    private SubjectDao subjectDao;
    private MarkDao markDao;

    public Testing() {
        try {
            DaoFactory factory = StaticFactory.getFactory(Source.JDBC);
            testDao = factory.getTestDao();
            questionDao = factory.getQuestionDao();
            answerDao = factory.getAnswerDao();
            subjectDao = factory.getSubjectDao();
            markDao = factory.getMarkDao();
        }
        catch (Exception e) {
            LOGGER.error("getting dao error: " + e);
        }
    }

    public JSONObject getTests(JSONObject request) {
        String login = request.getOrDefault("login", "none").toString();
        List<Test> tests = testDao.getTestsByTutor(login);
        return formResponse(tests);
    }

    public JSONObject getTests() {
        List<Test> tests = testDao.getTests();
        return formResponse(tests);
    }

    public JSONObject getTestById(JSONObject request) {
        JSONObject response = new JSONObject();
        int testId = Integer.parseInt(request.getOrDefault("id", -1).toString());
        Test test = testDao.getTestById(testId);
        if (test != null) {
            response.put("status", "success");
            response.put("test", formTest(test));
        }
        else {
            response.put("status", "error");
            response.put("cause", "data_base");
        }
        return response;
    }

    public JSONObject insertTest(JSONObject request) {
        JSONObject response = new JSONObject();
        Test test = getTest(request);
        int testId = testDao.insertTest(test);
        if (testId != -1) {
            response.put("status", "success");
            response.put("id", testId);
        }
        else {
            response.put("status", "error");
            response.put("cause", "data_base");
        }
        return response;
    }

    public JSONObject updateTest(JSONObject request) {
        JSONObject response = new JSONObject();
        Test test = getTest(request);
        if (testDao.updateTest(test)) {
            response.put("status", "success");
        }
        else {
            response.put("status", "error");
            response.put("cause", "data_base");
        }
        return response;
    }

    public JSONObject insertQuestion(JSONObject request) {
        JSONObject response = new JSONObject();
        Question question = getQuestion(request);
        int testId = Integer.parseInt(request.getOrDefault("testId", -1).toString());
        int questionId = questionDao.insertQuestion(question, testId);
        if (questionId != -1) {
            response.put("status", "success");
            response.put("id", questionId);
        }
        else {
            response.put("status", "error");
            response.put("cause", "data_base");
        }
        return response;
    }

    public JSONObject updateQuestion(JSONObject request) {
        JSONObject response = new JSONObject();
        Question question = getQuestion(request);
        if (questionDao.updateQuestion(question)) {
            response.put("status", "success");
        }
        else {
            response.put("status", "error");
            response.put("cause", "data_base");
        }
        return response;
    }

    public JSONObject insertAnswer(JSONObject request) {
        JSONObject response = new JSONObject();
        Answer answer = getAnswer(request);
        int questionId = Integer.parseInt(request.getOrDefault("questionId", -1).toString());
        int answerId = answerDao.insertAnswer(answer, questionId);
        if (answerId != -1) {
            response.put("status", "success");
            response.put("id", answerId);
        }
        else {
            response.put("status", "error");
            response.put("cause", "data_base");
        }
        return response;
    }

    public JSONObject updateAnswer(JSONObject request) {
        JSONObject response = new JSONObject();
        Answer answer = getAnswer(request);
        if (answerDao.updateAnswer(answer)) {
            response.put("status", "success");
        }
        else {
            response.put("status", "error");
            response.put("cause", "data_base");
        }
        return response;
    }

    public JSONObject deleteTest(JSONObject request) {
        JSONObject response = new JSONObject();
        int testId = Integer.parseInt(request.getOrDefault("id", -1).toString());
        if (testDao.deleteTest(testId)) {
            response.put("status", "success");
        }
        else {
            response.put("status", "error");
            response.put("cause", "data_base");
        }
        return response;
    }

    public JSONObject deleteQuestion(JSONObject request) {
        JSONObject response = new JSONObject();
        int questionId = Integer.parseInt(request.getOrDefault("id", -1).toString());
        if (questionDao.deleteQuestion(questionId)) {
            response.put("status", "success");
        }
        else {
            response.put("status", "error");
            response.put("cause", "data_base");
        }
        return response;
    }

    public JSONObject deleteAnswer(JSONObject request) {
        JSONObject response = new JSONObject();
        int answerId = Integer.parseInt(request.getOrDefault("id", -1).toString());
        if (answerDao.deleteAnswer(answerId)) {
            response.put("status", "success");
        }
        else {
            response.put("status", "error");
            response.put("cause", "data_base");
        }
        return response;
    }

    private Question getQuestion(JSONObject jsonQuestion) {
        Question question = new Question();
        question.setId(Integer.parseInt(jsonQuestion.getOrDefault("id", -1).toString()));
        question.setText(jsonQuestion.getOrDefault("text", "none").toString());
        return question;
    }

    private Answer getAnswer(JSONObject jsonAnswer) {
        Answer answer = new Answer();
        answer.setId(Integer.parseInt(jsonAnswer.getOrDefault("id", -1).toString()));
        answer.setText(jsonAnswer.getOrDefault("text", "none").toString());
        answer.setRight((Boolean) jsonAnswer.getOrDefault("right", false));
        return answer;
    }

    private JSONObject formTest(Test test) {
        JSONObject jsonTest = getJsonTest(test);
        List<Question> questions = questionDao.getQuestions(test.getId());
        JSONArray questionArray = new JSONArray();
        for (var question : questions) {
            JSONObject jsonQuestion = getJsonQuestion(question);
            List<Answer> answers = answerDao.getAnswers(question.getId());
            JSONArray answerArray = new JSONArray();
            for (var answer : answers) {
                answerArray.add(getJsonAnswer(answer));
            }
            jsonQuestion.put("answers", answerArray);
            questionArray.add(jsonQuestion);
        }
        jsonTest.put("questions", questionArray);
        return jsonTest;
    }

    private JSONObject formResponse(List<Test> tests) {
        JSONObject response = new JSONObject();
        if (tests != null) {
            if (tests.isEmpty()) {
                response.put("status", "warning");
                response.put("cause", "empty_data");
            }
            else {
                JSONArray testsArray = new JSONArray();
                for (var test : tests) {
                    testsArray.add(getJsonTest(test));
                }
                response.put("status", "success");
                response.put("tests", testsArray);
            }
        }
        else {
            response.put("status", "error");
            response.put("cause", "data_base");
        }
        return response;
    }

    private JSONObject getJsonTest(Test test) {
        JSONObject jsonTest = new JSONObject();
        jsonTest.put("id", test.getId());
        jsonTest.put("title", test.getTitle());
        jsonTest.put("subject", test.getSubject().getSubject());
        jsonTest.put("name", test.getUser().getName());
        jsonTest.put("surname", test.getUser().getSurname());
        return jsonTest;
    }

    private JSONObject getJsonQuestion(Question question) {
        JSONObject jsonQuestion = new JSONObject();
        jsonQuestion.put("id", question.getId());
        jsonQuestion.put("text", question.getText());
        return jsonQuestion;
    }

    private JSONObject getJsonAnswer(Answer answer) {
        JSONObject jsonAnswer = new JSONObject();
        jsonAnswer.put("id", answer.getId());
        jsonAnswer.put("text", answer.getText());
        jsonAnswer.put("right", answer.isRight());
        return jsonAnswer;
    }

    private Test getTest(JSONObject jsonTest) {
        Test test = new Test();
        test.setId(Integer.parseInt(jsonTest.getOrDefault("id", "-1").toString()));
        test.setTitle(jsonTest.getOrDefault("title", "none").toString());
        String subjectName = jsonTest.getOrDefault("subject", "none").toString();
        test.setSubject(getSubject(subjectName));
        User user = new User();
        user.setId(Integer.parseInt(jsonTest.getOrDefault("userId", "-1").toString()));
        return test;
    }

    private Subject getSubject(String subjectName) {
        Subject subject = new Subject();
        if (!subjectName.equals("none")) {
            subject = subjectDao.getSubject(subjectName);
            if (subject == null) {
                subject = new Subject();
                subject.setSubject(subjectName);
                subject.setId(subjectDao.insertSubject(subject));
            }
        }
        return subject;
    }
}
