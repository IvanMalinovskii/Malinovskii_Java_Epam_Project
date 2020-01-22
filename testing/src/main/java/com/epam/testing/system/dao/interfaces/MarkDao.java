package com.epam.testing.system.dao.interfaces;

import com.epam.testing.system.entities.Mark;

import java.util.List;

public interface MarkDao {
    List<Mark> getMarksByUser(int userId);
    List<Mark> getMarksByTest(int testId);
    int insertMark(Mark mark);
}
