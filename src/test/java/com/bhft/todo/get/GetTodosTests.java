package com.bhft.todo.get;


import com.bhft.todo.BaseTest;
import com.todo.requests.ValidatedTodoRequest;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.todo.specs.RequestSpec.unAuthSpec;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.todo.models.Todo;

import java.util.List;

@Epic("TODO Management")
@Feature("Get Todos API")
public class GetTodosTests extends BaseTest {

    @BeforeEach
    public void setupEach() {
        deleteAllTodos();
    }

    @Test
    @Description("Получение пустого списка TODO, когда база данных пуста")
    public void testGetTodosWhenDatabaseIsEmpty() {
        List<Todo> todos = new ValidatedTodoRequest(unAuthSpec()).readAll();
        assertThat("Список пуст", todos.size(), is(0));
    }

    @Test
    @Description("Получение списка TODO с существующими записями")
    public void testGetTodosWithExistingEntries() {
        // Предварительно создать несколько TODO
        Todo todo1 = new Todo(1, "Task 1", false);
        Todo todo2 = new Todo(2, "Task 2", true);
        new ValidatedTodoRequest(unAuthSpec()).create(todo1);
        new ValidatedTodoRequest(unAuthSpec()).create(todo2);

        List<Todo> todos = new ValidatedTodoRequest(unAuthSpec()).readAll();

        assertThat("Список содержит 2 значения", todos.size(), is(2));
        assertThat("Содержатся корректные данные", todos, containsInAnyOrder(todo1, todo2));
        //поленилась делать софт ассерт
    }

    @Test
    @Description("Использование параметров offset и limit для пагинации")
    public void testGetTodosWithOffsetAndLimit() {
        // Создаем 5 TODO
        for (int i = 1; i <= 5; i++) {
            new ValidatedTodoRequest(unAuthSpec()).create(new Todo(i, "Task " + i, i % 2 == 0));

        }

        List<Todo> todos = new ValidatedTodoRequest(unAuthSpec()).readAll(2, 2);

        assertThat("Список содержит 2 значения", todos.size(), is(2));
//        assertThat(); проассертить что в пагинаию попали таски с id 3 и id 4 (и сами таски корректны)
    }

    @Test
    @DisplayName("Передача некорректных значений в offset и limit")
    public void testGetTodosWithInvalidOffsetAndLimit() {
        // Тест с отрицательным offset
        given().filter(new AllureRestAssured())
                .queryParam("offset", -1)
                .queryParam("limit", 2)
                .when()
                .get("/todos")
                .then()
                .statusCode(400)
                .contentType("text/plain")
                .body(containsString("Invalid query string"));

        // Тест с нечисловым limit
        given().filter(new AllureRestAssured())
                .queryParam("offset", 0)
                .queryParam("limit", "abc")
                .when()
                .get("/todos")
                .then()
                .statusCode(400)
                .contentType("text/plain")
                .body(containsString("Invalid query string"));

        // Тест с отсутствующим значением offset
        given().filter(new AllureRestAssured())
                .queryParam("offset", "")
                .queryParam("limit", 2)
                .when()
                .get("/todos")
                .then()
                .statusCode(400)
                .contentType("text/plain")
                .body(containsString("Invalid query string"));
    }

    @Test
    @DisplayName("Проверка ответа при превышении максимально допустимого значения limit")
    public void testGetTodosWithExcessiveLimit() {
        // Создаем 10 TODO
        for (int i = 1; i <= 10; i++) {
            new ValidatedTodoRequest(unAuthSpec()).create(new Todo(i, "Task " + i, i % 2 == 0));
        }

        Response response = given().filter(new AllureRestAssured())
                .queryParam("limit", 1000)
                .when()
                .get("/todos")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract()
                .response();

        Todo[] todos = response.getBody()
                .as(Todo[].class);

        // Проверяем, что вернулось 10 задач
        Assertions.assertEquals(10, todos.length);
    }
}
