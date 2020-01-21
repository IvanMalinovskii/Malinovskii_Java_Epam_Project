package com.epam.testing.system.dao.interfaces;

import com.epam.testing.system.entities.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> getQuestions(int testId);
    boolean updateQuestion(Question question);
    boolean deleteQuestion(int questionId);
    int insertQuestion(Question question, int testId);
}
