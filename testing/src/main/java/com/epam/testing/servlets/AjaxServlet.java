package com.epam.testing.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * abstract class for parsing requests
 */
public abstract class AjaxServlet extends HttpServlet {
    protected static final Logger LOGGER = LogManager.getLogger(AjaxServlet.class);

    /**
     * parses requests into json format
     * @param request request
     * @return json object
     */
    protected JSONObject getJsonRequest(HttpServletRequest request) {
        try {
            BufferedReader reader = request.getReader();
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            JSONParser parser = new JSONParser();
            return (JSONObject) parser.parse(builder.toString());
        }
        catch (IOException e) {
            LOGGER.error("getting reader error: " + e);
        }
        catch (ParseException e) {
            LOGGER.error("parsing request error: " + e);
        }
        return null;
    }
}
