package com.epam.testing.system.dao.interfaces;

import com.epam.testing.system.entities.Subject;

/**
 * describes subject dao
 */
public interface SubjectDao {
    Subject getSubject(String subjectName);
    int insertSubject(Subject subject);
}
