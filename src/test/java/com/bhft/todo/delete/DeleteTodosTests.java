package com.bhft.todo.delete;

import com.bhft.todo.BaseTest;
import com.todo.models.Todo;
import com.todo.models.TodosBuilder;
import com.todo.requests.TodoRequest;
import com.todo.requests.ValidatedTodoRequest;
import com.todo.specs.RequestSpec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.todo.specs.RequestSpec.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.is;

public class DeleteTodosTests extends BaseTest {

    /**
     * TC1: Успешное удаление существующего TODO с корректной авторизацией.
     */
    @Test
    public void testDeleteExistingTodoWithValidAuth() {
        Todo todo = new TodosBuilder().setId(1)
                .setText("Task to Delete")
                .setCompleted(false)
                .build();
        new ValidatedTodoRequest(unAuthSpec()).create(todo);
        String body = new ValidatedTodoRequest(RequestSpec.authSpec()).delete(todo.getId());
        assertThat(body, is(emptyOrNullString()));
        // Получаем список всех TODO и проверяем, что удаленная задача отсутствует
        List<Todo> todos = new ValidatedTodoRequest(authSpec()).readAll();
        Assertions.assertTrue(todos.stream()
                .noneMatch(todoFromList -> todoFromList.equals(todo)), "Задача удалена из списка");
    }

    /**
     * TC2: Попытка удаления TODO без заголовка Authorization.
     */
    @Test
    public void testDeleteTodoWithoutAuthHeader() {
        // Создаем TODO для удаления
        Todo todo = new TodosBuilder().setId(2)
                .setText("Task to Delete")
                .setCompleted(false)
                .build();
        new ValidatedTodoRequest(unAuthSpec()).create(todo);
        // Отправляем DELETE запрос без заголовка Authorization
        new TodoRequest(unAuthSpec()).delete(todo.getId())
                .then()
                .statusCode(401);
        List<Todo> todos = new ValidatedTodoRequest(unAuthSpec()).readAll();
        // Проверяем, что TODO не было удалено
        Assertions.assertTrue(todos.contains(todo), "Задача не удалена");
    }

    /**
     * TC3: Попытка удаления TODO с некорректными учетными данными.
     */
    @Test
    public void testDeleteTodoWithInvalidAuth() {
        // Создаем TODO для удаления
        Todo todo = new TodosBuilder().setId(3)
                .setText("Task to Delete")
                .setCompleted(false)
                .build();
        new ValidatedTodoRequest(unAuthSpec()).create(todo);

        // Отправляем DELETE запрос с некорректной авторизацией
        new TodoRequest(invalidAuthSpec()).delete(todo.getId())
                .then()
                .statusCode(401);
        List<Todo> todos = new ValidatedTodoRequest(unAuthSpec()).readAll();
        // Проверяем, что TODO не было удалено
        Assertions.assertTrue(todos.contains(todo), "Задача не удалена");
    }

    /**
     * TC4: Удаление TODO с несуществующим id.
     */
    @Test
    public void testDeleteNonExistentTodo() {
        // Отправляем DELETE запрос для несуществующего TODO с корректной авторизацией
        Todo todo = new TodosBuilder().setId(999)
                .setText("Task to Delete")
                .setCompleted(false)
                .build();
        new TodoRequest(authSpec()).delete(todo.getId())
                .then()
                .statusCode(404);
        // В данном случае, поскольку мы не добавляли задач с id 999, список должен быть пуст или содержать только ранее добавленные задачи
    }

//    /**
//     * TC5: Попытка удаления с некорректным форматом id (например, строка вместо числа).
//     */
//    @Test
//    public void testDeleteTodoWithInvalidIdFormat() {
//        // Отправляем DELETE запрос с некорректным id
//        given()
//                .filter(new AllureRestAssured())
//                .auth()
//                .preemptive()
//                .basic("admin", "admin")
//                .when()
//                .delete("/todos/invalidId")
//                .then()
//                .statusCode(404);
////                .contentType(ContentType.JSON)
////                .body("error", notNullValue());
//    }
}
