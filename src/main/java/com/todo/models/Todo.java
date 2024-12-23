package com.todo.models;

import java.util.Objects;

public class Todo {
    private long id;
    private String text;
    private boolean completed;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Todo() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return getId() == todo.getId() && isCompleted() == todo.isCompleted() && Objects.equals(getText(), todo.getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getText(), isCompleted());
    }

    public Todo(long id, String text, boolean completed) {
        this.id = id;
        this.text = text;
        this.completed = completed;
    }
}
