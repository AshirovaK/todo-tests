package com.todo.requests.interfaces;

public interface SearchInterface {

    Object readAll(int offset, int limit);

    Object readAll();
}