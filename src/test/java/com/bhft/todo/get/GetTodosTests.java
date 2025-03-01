package com.bhft.todo.get;


import com.bhft.todo.BaseTest;
import com.todo.annotations.PrepareTodo;
import com.todo.annotations.DataPreparationExtension;
import com.todo.requests.ValidatedTodoRequest;
import com.todo.storages.TestDataStorage;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.todo.specs.request.RequestSpec.unAuthSpec;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.todo.models.Todo;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.List;

@Epic("TODO Management")
@Feature("Get Todos API")
@ExtendWith(DataPreparationExtension.class)
//TODO можно ли делать несколько расширений аннотаций?
public class GetTodosTests extends BaseTest {

    @Test
    @Description("Получение пустого списка TODO, когда база данных пуста")
    public void testGetTodosWhenDatabaseIsEmpty() {
        List<Todo> todos = unAuthTodoRequester.getValidatedRequest()
                .readAll();
        assertThat("Список пуст", todos.size(), is(0));
    }

    @Test
    @Description("Получение списка TODO с существующими записями")
    @PrepareTodo(2)
    public void testGetTodosWithExistingEntries() {
        HashMap<Long, Todo> todosStorage = TestDataStorage.getInstance()
                .getStorage();
        List<Todo> todos = unAuthTodoRequester.getValidatedRequest()
                .readAll();

        assertThat("Список содержит 2 значения", todos.size(), is(2));
        assertThat("Содержатся корректные данные", todos.containsAll(todosStorage.values()));
    }

    @Test
    @Description("Использование параметров offset и limit для пагинации")
    @PrepareTodo(5)
    public void testGetTodosWithOffsetAndLimit() {
        List<Todo> todos = unAuthTodoRequester.getValidatedRequest()
                .readAll(2, 2);
        assertThat("Список содержит 2 значения", todos.size(), is(2));
//        assertThat(); проассертить что в пагинаию попали таски с id 3 и id 4 (и сами таски корректны)
    }

//    @Test
//    @DisplayName("Передача некорректных значений в offset и limit")
//    public void testGetTodosWithInvalidOffsetAndLimit() {
//        // Тест с отрицательным offset
//        given().filter(new AllureRestAssured())
//                .queryParam("offset", -1)
//                .queryParam("limit", 2)
//                .when()
//                .get("/todos")
//                .then()
//                .statusCode(400)
//                .contentType("text/plain")
//                .body(containsString("Invalid query string"));
//
//        // Тест с нечисловым limit
//        given().filter(new AllureRestAssured())
//                .queryParam("offset", 0)
//                .queryParam("limit", "abc")
//                .when()
//                .get("/todos")
//                .then()
//                .statusCode(400)
//                .contentType("text/plain")
//                .body(containsString("Invalid query string"));
//
//        // Тест с отсутствующим значением offset
//        given().filter(new AllureRestAssured())
//                .queryParam("offset", "")
//                .queryParam("limit", 2)
//                .when()
//                .get("/todos")
//                .then()
//                .statusCode(400)
//                .contentType("text/plain")
//                .body(containsString("Invalid query string"));
//    }

    @Test
    @DisplayName("Проверка ответа при превышении максимально допустимого значения limit")
    @PrepareTodo(10)
    public void testGetTodosWithExcessiveLimit() {
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
