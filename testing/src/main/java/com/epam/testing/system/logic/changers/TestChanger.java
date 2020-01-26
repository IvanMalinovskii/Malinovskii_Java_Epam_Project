package com.epam.testing.system.logic.changers;

import com.epam.testing.system.dao.interfaces.SubjectDao;
import com.epam.testing.system.dao.interfaces.TestDao;
import com.epam.testing.system.entities.Subject;
import com.epam.testing.system.entities.Test;
import com.epam.testing.system.entities.User;
import org.json.simple.JSONObject;

public class TestChanger extends Changer {
    private TestDao testDao;
    private SubjectDao subjectDao;

    public TestChanger() {
        super();
        testDao = factory.getTestDao();
        subjectDao = factory.getSubjectDao();
    }

    @Override
    public JSONObject insertEntity(JSONObject request) {
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

    @Override
    public JSONObject updateEntity(JSONObject request) {
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

    @Override
    public JSONObject deleteEntity(JSONObject request) {
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

    private Test getTest(JSONObject jsonTest) {
        Test test = new Test();
        test.setId(Integer.parseInt(jsonTest.getOrDefault("id", "-1").toString()));
        test.setTitle(jsonTest.getOrDefault("title", "none").toString());
        String subjectName = jsonTest.getOrDefault("subject", "none").toString();
        test.setSubject(getSubject(subjectName));
        User user = new User();
        user.setId(Integer.parseInt(jsonTest.getOrDefault("userId", "-1").toString()));
        test.setUser(user);
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
