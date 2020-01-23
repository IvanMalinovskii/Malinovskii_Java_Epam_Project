package com.epam.testing.servlets.testing;

import com.epam.testing.servlets.AjaxServlet;
import com.epam.testing.system.logic.Testing;
import com.epam.testing.system.logic.changers.AnswerChanger;
import com.epam.testing.system.logic.changers.Changer;
import com.epam.testing.system.logic.changers.QuestionChanger;
import com.epam.testing.system.logic.changers.TestChanger;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/question")
public class ChangeTestServlet extends AjaxServlet {
    private Testing testing;

    @Override
    public void init() throws ServletException {
        super.init();
        testing = new Testing();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject jsonRequest = getJsonRequest(req);
        JSONObject jsonResponse = formResponse(jsonRequest);

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(jsonResponse.toJSONString());
        writer.close();
    }

    private JSONObject formResponse(JSONObject jsonRequest) {
        JSONObject jsonResponse;
        Changer changer = getChanger(jsonRequest.getOrDefault("entity", "none").toString());
        if (changer != null) {
            switch (jsonRequest.getOrDefault("action", "none").toString()) {
                case "insert":
                    jsonResponse = changer.insertEntity(jsonRequest);
                    break;
                case "update":
                    jsonResponse = changer.updateEntity(jsonRequest);
                    break;
                case "delete":
                    jsonResponse = changer.deleteEntity(jsonRequest);
                    break;
                default:
                    jsonResponse = new JSONObject();
                    jsonResponse.put("status", "error");
                    jsonResponse.put("cause", "invalid_action");
                    LOGGER.warn("invalid request");
                    break;
            }
        }
        else {
            jsonResponse = new JSONObject();
            jsonResponse.put("status", "error");
            jsonResponse.put("cause", "invalid request");
        }
        return jsonResponse;
    }

    private Changer getChanger(String entity) {
        switch (entity) {
            case "test":
                return new TestChanger();
            case "question":
                return new QuestionChanger();
            case "answer":
                return new AnswerChanger();
            default:
                LOGGER.error("invalid entity type");
                return null;
        }
    }
}
