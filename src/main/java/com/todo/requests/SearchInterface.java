package com.todo.requests;

public interface SearchInterface {

    Object readAll(int offset, int limit);

    Object readAll();
}