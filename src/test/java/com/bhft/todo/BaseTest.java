package com.bhft.todo;

import com.todo.conf.Configuration;
import com.todo.requests.AuthType;
import com.todo.requests.TodoRequesterFactory;
import com.todo.requests.facades.TodoRequester;
import com.todo.storages.TestDataCleaner;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {

    protected TodoRequester todoRequester;
    protected TodoRequester unAuthTodoRequester;
    protected TodoRequester invalidAuthTodoRequester;

    @BeforeAll
    public static void setup() {
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.baseURI = Configuration.getInstance()
                .getProperty("BASE_URL");
        RestAssured.port = Integer.parseInt(Configuration.getInstance()
                .getProperty("PORT"));
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
