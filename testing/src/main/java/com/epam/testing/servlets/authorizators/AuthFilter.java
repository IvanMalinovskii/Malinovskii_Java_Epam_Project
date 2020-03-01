package com.epam.testing.servlets.authorizators;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * filters requests to servlets and pages
 */
@WebFilter(servletNames = {"AssessmentServlet", "ChangeTestServlet", "ReadTestServlet"},
           urlPatterns = {"/front/pages/testing.html"})
public class AuthFilter implements Filter {
    private static final Logger LOGGER = LogManager.getLogger(AuthFilter.class);
    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        LOGGER.info("filter has been initialized");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession(false);
        if (session != null) {
            filterChain.doFilter(servletRequest, servletResponse);
        }
        else {
            RequestDispatcher dispatcher = filterConfig.getServletContext()
                    .getRequestDispatcher("/front/pages/authorization.html");
            dispatcher.forward(servletRequest, servletResponse);
            /*
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "redirect");
            jsonResponse.put("link", request.getContextPath() + "/front/authorization.html");
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            writer.write(jsonResponse.toJSONString());
            writer.close();
            */
        }
    }

    @Override
    public void destroy() {
        LOGGER.info("filter has been destroyed");
        filterConfig = null;
    }
}
