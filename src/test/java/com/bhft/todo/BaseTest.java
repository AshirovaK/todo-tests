package com.bhft.todo;

import com.todo.conf.Configuration;
import com.todo.models.Todo;
import com.todo.requests.TodoRequest;
import com.todo.specs.RequestSpec;
import com.todo.storages.TestDataStorage;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.List;

public class BaseTest {
    @BeforeAll
    public static void setup() {
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.baseURI = Configuration.getInstance()
                .getProperty("BASE_URL");
        RestAssured.port = Integer.parseInt(Configuration.getInstance()
                .getProperty("PORT"));
    }

    @BeforeEach
    protected void deleteAllTodos() {
        List<Todo> todos = List.of(new TodoRequest(RequestSpec.authSpec()).readAll()
                .then()
                .extract()
                .body()
                .as(Todo[].class));
        todos.forEach(todo -> new TodoRequest(RequestSpec.authSpec()).delete(todo.getId()));
    }

    @AfterEach
    public void clean() {
        HashMap<Long, Todo> todos = TestDataStorage.getInstance()
                .getStorage();
        todos.forEach((id, todo) -> new TodoRequest(RequestSpec.authSpec()).delete(id));
//        TestDataStorage.getInstance()
//                .cleanInstance();
    }

}
