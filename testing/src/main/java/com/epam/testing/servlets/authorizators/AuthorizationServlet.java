package com.epam.testing.servlets.authorizators;

import com.epam.testing.servlets.AjaxServlet;
import com.epam.testing.system.logic.Authorization;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/authorization")
public class AuthorizationServlet extends AjaxServlet {
    Authorization authorization;

    @Override
    public void init() throws ServletException {
        super.init();
        authorization = new Authorization();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject jsonRequest = getJsonRequest(req);
        JSONObject jsonResponse;
        switch (jsonRequest.getOrDefault("action", "none").toString()) {
            case "signUp":
                jsonResponse = authorization.doSignUp(jsonRequest);
                addSession(req, jsonResponse);
                break;
            case "signIn":
                jsonResponse = authorization.doSignIn(jsonRequest);
                addSession(req, jsonResponse);
                break;
            default:
                jsonResponse = new JSONObject();
                jsonResponse.put("status", "error");
                jsonResponse.put("cause", "invalid_action");
                break;
        }
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(jsonResponse.toJSONString());
        writer.close();
    }

    private void addSession(HttpServletRequest req, JSONObject jsonResponse) {
        if (jsonResponse.get("status").toString().equals("success")) {
            HttpSession session = req.getSession(true);
        }
    }
}
