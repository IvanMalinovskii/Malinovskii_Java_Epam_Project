package com.epam.testing.servlets.testing;

import com.epam.testing.servlets.AjaxServlet;
import com.epam.testing.system.logic.Testing;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class ReadTestServlet extends AjaxServlet {
    Testing testing;

    @Override
    public void init() throws ServletException {
        super.init();
        testing = new Testing();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject jsonRequest = getJsonRequest(req);
        JSONObject jsonResponse;

        switch (jsonRequest.getOrDefault("action", "none").toString()) {
            case "getAll":
                jsonResponse = testing.getTests();
                break;
            case "getByUser":
                jsonResponse = testing.getTests(jsonRequest);
                break;
            case "getById":
                jsonResponse = testing.getTestById(jsonRequest);
                break;
            default:
                jsonResponse = new JSONObject();
                jsonResponse.put("status", "error");
                jsonResponse.put("cause", "invalid_action");
                LOGGER.warn("invalid request");
        }
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(jsonResponse.toJSONString());
        writer.close();
    }
}
