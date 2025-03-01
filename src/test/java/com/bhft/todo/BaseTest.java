package com.bhft.todo;

import com.todo.config.RestAssuredConfig;
import com.todo.requests.AuthType;
import com.todo.requests.TodoRequesterFactory;
import com.todo.requests.facades.TodoRequester;
import com.todo.storages.TestDataCleaner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {

    protected TodoRequester todoRequester;
    protected TodoRequester unAuthTodoRequester;
    protected TodoRequester invalidAuthTodoRequester;

    @BeforeAll
    public static void setup() {
        RestAssuredConfig.setup();
    }

    @BeforeEach
    public void setupTest() {
        todoRequester = TodoRequesterFactory.createRequester(AuthType.AUTHENTICATED);
        unAuthTodoRequester = TodoRequesterFactory.createRequester(AuthType.UNAUTHENTICATED);
        invalidAuthTodoRequester = TodoRequesterFactory.createRequester(AuthType.INVALID_AUTH);
    }

//    @BeforeEach
//    protected void deleteAllTodos() {
//        List<Todo> todos = List.of(new TodoRequest(RequestSpec.authSpec()).readAll()
//                .then()
//                .extract()
//                .body()
//                .as(Todo[].class));
//        todos.forEach(todo -> new TodoRequest(RequestSpec.authSpec()).delete(todo.getId()));
//    }

    @AfterEach
    public void clean() {
        TestDataCleaner.clean();
    }

}
