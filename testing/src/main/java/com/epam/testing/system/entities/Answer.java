package com.epam.testing.system.entities;

/**
 * describes an answer
 */
public class Answer {
    private int id;
    private String text;
    private boolean right;

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

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return text;
    }

}
