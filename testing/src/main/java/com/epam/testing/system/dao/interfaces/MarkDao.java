package com.epam.testing.system.dao.interfaces;

import com.epam.testing.system.entities.Mark;

import java.util.List;

/**
 * describes mark dao
 */
public interface MarkDao {
    List<Mark> getMarksByUser(int userId, int testId);
    List<Mark> getMarksByTest(int testId);
    int insertMark(Mark mark);
}
