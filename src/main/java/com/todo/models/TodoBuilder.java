package com.todo.models;

//Почему класс и его Builder создаются отдельно?
//
//
//1️⃣ Сделать класс неизменяемым (Immutable)
//2️⃣ Упростить инициализацию сложных объектов
//3️⃣ Избежать перегруженных конструкторов
//4️⃣ Сохранить читаемость кода



public class TodoBuilder {
    private long id;
    private String text;
    private boolean completed;

    public TodoBuilder() {

    }

    public TodoBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public TodoBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public TodoBuilder setCompleted(boolean completed) {
        this.completed = completed;
        return this;
    }

    public Todo build() {
        return new Todo(id, text, completed);
    }
}
