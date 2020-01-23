package com.epam.testing.system.logic.changers;

import com.epam.testing.system.dao.interfaces.AnswerDao;
import com.epam.testing.system.entities.Answer;
import org.json.simple.JSONObject;

public class AnswerChanger extends Changer {
    private AnswerDao answerDao;

    public AnswerChanger() {
        super();
        answerDao = factory.getAnswerDao();
    }

    @Override
    public JSONObject insertEntity(JSONObject request) {
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

    @Override
    public JSONObject updateEntity(JSONObject request) {
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

    @Override
    public JSONObject deleteEntity(JSONObject request) {
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

    private Answer getAnswer(JSONObject jsonAnswer) {
        Answer answer = new Answer();
        answer.setId(Integer.parseInt(jsonAnswer.getOrDefault("id", -1).toString()));
        answer.setText(jsonAnswer.getOrDefault("text", "none").toString());
        answer.setRight((Boolean) jsonAnswer.getOrDefault("right", false));
        return answer;
    }
}
