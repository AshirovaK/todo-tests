package com.todo.requests.facades;

import com.todo.requests.TodoRequest;
import com.todo.requests.ValidatedTodoRequest;
import io.restassured.specification.RequestSpecification;

public class TodoRequester {

    private TodoRequest todoRequest;
    private ValidatedTodoRequest validatedTodoRequest;

    public TodoRequester(RequestSpecification requestSpecification) {
        this.todoRequest = new TodoRequest(requestSpecification);
        this.validatedTodoRequest = new ValidatedTodoRequest(requestSpecification);
    }

    public TodoRequest getRequest() {
        return todoRequest;
    }

    public ValidatedTodoRequest getValidatedRequest() {
        return validatedTodoRequest;
    }

}
