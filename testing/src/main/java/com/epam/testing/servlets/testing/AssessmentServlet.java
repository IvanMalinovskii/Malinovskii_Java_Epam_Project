package com.epam.testing.servlets.testing;

import com.epam.testing.servlets.AjaxServlet;
import com.epam.testing.system.logic.Assessment;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * received requests for working with marks
 */
@WebServlet("/marks")
public class AssessmentServlet extends AjaxServlet {
    Assessment assessment;

    @Override
    public void init() throws ServletException {
        super.init();
        assessment = new Assessment();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject jsonRequest = getJsonRequest(req);
        JSONObject jsonResponse;

        switch (jsonRequest.getOrDefault("action", "none").toString()) {
            case "getByTest":
                jsonResponse = assessment.getMarksByTest(jsonRequest);
                break;
            case "getByUser":
                jsonResponse = assessment.getMarksByUser(jsonRequest);
                break;
            case "insert":
                jsonResponse = assessment.insertMark(jsonRequest);
                break;
            default:
                jsonResponse = new JSONObject();
                jsonResponse.put("status", "error");
                jsonResponse.put("cause", "invalid request");
                LOGGER.error("invalid request");
                break;
        }
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(jsonResponse.toJSONString());
        writer.close();
    }
}
