package com.epam.testing.system.logic.changers;

import com.epam.testing.system.dao.interfaces.QuestionDao;
import com.epam.testing.system.entities.Question;
import org.json.simple.JSONObject;

/**
 * describes a question changer
 */
public class QuestionChanger extends Changer {
    private QuestionDao questionDao;

    public QuestionChanger() {
        super();
        questionDao = factory.getQuestionDao();
    }

    @Override
    public JSONObject insertEntity(JSONObject request) {
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

    @Override
    public JSONObject updateEntity(JSONObject request) {
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

    @Override
    public JSONObject deleteEntity(JSONObject request) {
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

    private Question getQuestion(JSONObject jsonQuestion) {
        Question question = new Question();
        question.setId(Integer.parseInt(jsonQuestion.getOrDefault("id", -1).toString()));
        question.setText(jsonQuestion.getOrDefault("text", "none").toString());
        return question;
    }
}
