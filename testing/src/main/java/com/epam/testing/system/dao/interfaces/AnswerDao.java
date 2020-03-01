package com.epam.testing.system.dao.interfaces;

import com.epam.testing.system.entities.Answer;

import java.util.List;

/**
 * describes answer dao
 */
public interface AnswerDao {
    List<Answer> getAnswers(int questionId);
    boolean updateAnswer(Answer answer);
    boolean deleteAnswer(int answerId);
    int insertAnswer(Answer answer, int questionId);
}
