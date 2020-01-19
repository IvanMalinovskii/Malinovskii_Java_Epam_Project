package com.epam.testing.system.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * describes a test
 */
public class Test {
    private int id;
    private String title;
    private List<Question> questions;

    {
        questions = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Question getQuestion(int index) {
        return questions.get(index);
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }
}
