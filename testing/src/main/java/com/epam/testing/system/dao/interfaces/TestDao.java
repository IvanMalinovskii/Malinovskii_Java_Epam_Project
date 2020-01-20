package com.epam.testing.system.dao.interfaces;

import com.epam.testing.system.entities.Test;

import java.util.List;

/**
 * describes methods for all the test daos
 */
public interface TestDao {
    List<Test> getTests();
    List<Test> getTestsByTutor(String tutorLogin);
    Test getTestById(int testId);
    int insertTest(Test test);
    boolean updateTest(Test test);
    boolean deleteTest(int testId);
}
