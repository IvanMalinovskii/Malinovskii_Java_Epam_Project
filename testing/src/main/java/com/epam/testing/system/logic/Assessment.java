package com.epam.testing.system.logic;

import com.epam.testing.system.dao.factories.DaoFactory;
import com.epam.testing.system.dao.factories.Source;
import com.epam.testing.system.dao.factories.StaticFactory;
import com.epam.testing.system.dao.interfaces.MarkDao;
import com.epam.testing.system.dao.interfaces.SubjectDao;
import com.epam.testing.system.entities.Answer;
import com.epam.testing.system.entities.Mark;
import com.epam.testing.system.entities.Test;
import com.epam.testing.system.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

/**
 * describes logic for working with answers daos
 */
public class Assessment {
    private MarkDao markDao;
    private static final Logger LOGGER = LogManager.getLogger(Assessment.class);

    public Assessment() {
        try {
            DaoFactory factory = StaticFactory.getFactory(Source.JDBC);
            markDao = factory.getMarkDao();
        }
        catch (Exception e) {
            LOGGER.error("getting factory error: " + e);
        }
    }

    public JSONObject getMarksByUser(JSONObject request) {
        int userId = Integer.parseInt(request.getOrDefault("userId", -1).toString());
        int testId = Integer.parseInt(request.getOrDefault("testId", -1).toString());
        List<Mark> marks = markDao.getMarksByUser(userId, testId);
        return getResponse(marks);
    }

    public JSONObject getMarksByTest(JSONObject request) {
        int testId = Integer.parseInt(request.getOrDefault("testId", -1).toString());
        List<Mark> marks = markDao.getMarksByTest(testId);
        return getResponse(marks);
    }

    public JSONObject insertMark(JSONObject request) {
        JSONObject response = new JSONObject();
        Mark mark = getMark(request);
        int markId = markDao.insertMark(mark);
        if (markId != -1) {
            response.put("status", "success");
            response.put("id", markId);
        }
        else {
            response.put("status", "error");
            response.put("cause", "data_base");
        }
        return response;
    }

    private JSONObject getResponse(List<Mark> marks) {
        JSONObject response = new JSONObject();
        if (marks != null) {
            response.put("status", "success");
            response.put("marks", getMarkArray(marks));
        }
        else {
            response.put("status", "error");
            response.put("cause", "data_base");
        }
        return response;
    }

    private JSONArray getMarkArray(List<Mark> marks) {
        JSONArray markArray = new JSONArray();
        for (var mark : marks) {
            markArray.add(getJsonMark(mark));
        }
        return markArray;
    }

    private JSONObject getJsonMark(Mark mark) {
        JSONObject jsonMark = new JSONObject();
        jsonMark.put("id", mark.getId());
        jsonMark.put("value", mark.getValue());
        jsonMark.put("testId", mark.getTest().getId());
        jsonMark.put("testTitle", mark.getTest().getTitle());
        jsonMark.put("userId", mark.getUser().getId());
        jsonMark.put("userLogin", mark.getUser().getLogin());
        return jsonMark;
    }

    private Mark getMark(JSONObject jsonMark) {
        Mark mark = new Mark();
        mark.setId(Integer.parseInt(jsonMark.getOrDefault("id", -1).toString()));
        mark.setValue(Integer.parseInt(jsonMark.getOrDefault("value", 0).toString()));
        Test test = new Test();
        test.setId(Integer.parseInt(jsonMark.getOrDefault("testId", -1).toString()));
        mark.setTest(test);
        User user = new User();
        user.setId(Integer.parseInt(jsonMark.getOrDefault("userId", -1).toString()));
        mark.setUser(user);
        return mark;
    }
}
