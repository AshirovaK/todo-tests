package com.bhft.todo.put;

import com.bhft.todo.BaseTest;
import com.todo.models.TodoBuilder;
import com.todo.requests.TodoRequest;
import com.todo.requests.ValidatedTodoRequest;
import com.todo.specs.response.IncorrectDataResponse;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.todo.specs.request.RequestSpec.authSpec;
import static com.todo.specs.request.RequestSpec.unAuthSpec;
import static io.restassured.RestAssured.given;

import com.todo.models.Todo;

import java.util.List;

public class PutTodosTests extends BaseTest {

    /**
     * TC1: Обновление существующего TODO корректными данными.
     */
    @Test
    public void testUpdateExistingTodoWithValidData() {
        // Создаем TODO для обновления
        Todo originalTodo = new TodoBuilder().setId(1)
                .setText("Original Task")
                .setCompleted(false)
                .build();
        unAuthTodoRequester.getValidatedRequest()
                .create(originalTodo);

        // Обновленные данные
        Todo updatedTodo = new TodoBuilder().setId(1)
                .setText("Updated Task")
                .setCompleted(true)
                .build();

        // Отправляем PUT запрос для обновления
        given().filter(new ResponseLoggingFilter())
                .contentType(ContentType.JSON)
                .body(updatedTodo)
                .when()
                .put("/todos/" + updatedTodo.getId())
                .then()
                .statusCode(200);

        // Проверяем, что данные были обновлены
        Todo[] todos = given().when()
                .get("/todos")
                .then()
                .statusCode(200)
                .extract()
                .as(Todo[].class);

        Assertions.assertEquals(1, todos.length);
        Assertions.assertEquals("Updated Task", todos[0].getText());
        Assertions.assertTrue(todos[0].isCompleted());
    }

    /**
     * TC2: Попытка обновления TODO с несуществующим id.
     */
    @Test
    public void testUpdateNonExistentTodo() {
        // Обновленные данные для несуществующего TODO
        Todo updatedTodo = new TodoBuilder().setId(999)
                .setText("Non-existent Task")
                .setCompleted(true)
                .build();

        unAuthTodoRequester.getRequest()
                .update(999, updatedTodo)
                .then()
                .spec(new IncorrectDataResponse().notFound());
    }

    /**
     * TC3: Обновление TODO с отсутствием обязательных полей.
     */
    @Test
    public void testUpdateTodoWithMissingFields() {
        // Создаем TODO для обновления
        Todo originalTodo = new TodoBuilder().setId(2)
                .setText("Task to Update")
                .setCompleted(false)
                .build();
        todoRequester.getValidatedRequest()
                .create(originalTodo);

        // Обновленные данные с отсутствующим полем 'text'
        Todo invalidTodoJson = new TodoBuilder().setId(2)
                .setCompleted(false)
                .build();
        todoRequester.getRequest()
                .update(2, invalidTodoJson)
                .then()
                .spec(new IncorrectDataResponse().badRequest());
    }

    /**
     * TC4: Передача некорректных типов данных при обновлении.
     */
    @Disabled("Ignoring contract test")
    @Test
    public void testUpdateTodoWithInvalidDataTypes() {
        // Создаем TODO для обновления
        Todo originalTodo = new TodoBuilder().setId(3)
                .setText("Another Task")
                .setCompleted(false)
                .build();
        unAuthTodoRequester.getValidatedRequest()
                .create(originalTodo);

        // Обновленные данные с некорректным типом поля 'completed'
        String invalidTodoJson = "{ \"id\": 3, \"text\": \"Updated Task\", \"completed\": \"notBoolean\" }";

        given().filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(invalidTodoJson)
                .when()
                .put("/todos/3")
                .then()
                .statusCode(401);
    }

    /**
     * TC5: Обновление TODO без изменения данных (передача тех же значений).
     */
    @Test
    public void testUpdateTodoWithoutChangingData() {
        // Создаем TODO для обновления
        Todo originalTodo = new TodoBuilder().setId(4)
                .setText("Task without Changes")
                .setCompleted(false)
                .build();
        unAuthTodoRequester.getValidatedRequest()
                .create(originalTodo);

        // Отправляем PUT запрос с теми же данными

        todoRequester.getRequest()
                .update(4, originalTodo);

        List<Todo> todos = todoRequester.getValidatedRequest()
                .readAll();
        // Проверяем, что данные не изменились
        Assertions.assertEquals("Task without Changes", todos.getFirst()
                .getText());
        Assertions.assertFalse(todos.getFirst()
                .isCompleted());
    }
}
