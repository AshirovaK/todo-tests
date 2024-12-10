package com.todo.requests;

import com.todo.models.Todo;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ValidatedTodoRequest extends Request implements CrudInterface<Todo>, ReadInterface {

    public ValidatedTodoRequest(RequestSpecification reqSpec) {
        super(reqSpec);
    }

    protected TodoRequest todoRequest() {
        return new TodoRequest(reqSpec);
    }

    @Override
    public String create(Todo entity) {
        Response response = todoRequest().create(entity);
        assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));
        assertThat((response.body().asString()), is(Matchers.emptyOrNullString()));
        return response.body().asString();
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
    public Todo[] readAll() {
        Response response = todoRequest().readAll();
        assertThat(response.statusCode(), is(HttpStatus.SC_OK));
        return response.body().as(Todo[].class);
    }
}
