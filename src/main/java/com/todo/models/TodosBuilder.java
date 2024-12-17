package com.todo.models;

public class TodosBuilder {
    private long id;
    private String text;
    private boolean completed;

    public TodosBuilder() {

    }

    public TodosBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public TodosBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public TodosBuilder setCompleted(boolean completed) {
        this.completed = completed;
        return this;
    }

    public Todo build() {
        return new Todo(id, text, completed);
    }
}
