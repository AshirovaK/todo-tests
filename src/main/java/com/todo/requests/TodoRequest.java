package com.todo.requests;

import com.todo.models.Todo;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class TodoRequest extends Request implements CrudInterface<Todo>, ReadInterface {
    private static final String TODO_ENDPOINT = "/todos";

    public TodoRequest(RequestSpecification reqSpec) {
        super(reqSpec);
    }

    @Override
    public Response create(Todo entity) {
        return given()
                .spec(reqSpec)
                .body(entity)
                .when()
                .post(TODO_ENDPOINT);
    }

    @Override
    public Object update(long id, Todo entity) {
        return null;
    }

    @Override
    public Object delete(long id) {
        return null;
    }

    @Override
    public Response readAll() {
        return given().spec(reqSpec).when().get(TODO_ENDPOINT);
    }
}
