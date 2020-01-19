package com.epam.testing.system.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * describes a question
 */
public class Question {
    private int id;
    private String text;
    private List<Answer> answers;

    {
        answers = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Answer getAnswer(int index) {
        return answers.get(index);
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    @Override
    public String toString() {
        return text + ":\n" + answers.toString();
    }
}
