package com.bhft.todo.delete;

import com.bhft.todo.BaseTest;
import com.todo.models.Todo;
import com.todo.models.TodoBuilder;
import com.todo.requests.TodoRequest;
import com.todo.requests.ValidatedTodoRequest;
import com.todo.specs.request.RequestSpec;
import com.todo.specs.response.IncorrectDataResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.todo.specs.request.RequestSpec.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.is;

public class DeleteTodosTests extends BaseTest {

    /**
     * TC1: Успешное удаление существующего TODO с корректной авторизацией.
     */
    @Test
    public void testDeleteExistingTodoWithValidAuth() {
        Todo todo = new TodoBuilder().setId(1)
                .setText("Task to Delete")
                .setCompleted(false)
                .build();
        todoRequester.getValidatedRequest()
                .create(todo);
        String body = todoRequester.getValidatedRequest()
                .delete(todo.getId());
        assertThat(body, is(emptyOrNullString()));
        // Получаем список всех TODO и проверяем, что удаленная задача отсутствует
        List<Todo> todos = todoRequester.getValidatedRequest()
                .readAll();
        Assertions.assertTrue(todos.stream()
                .noneMatch(todoFromList -> todoFromList.equals(todo)), "Задача удалена из списка");
    }

    /**
     * TC2: Попытка удаления TODO без заголовка Authorization.
     */
    @Test
    public void testDeleteTodoWithoutAuthHeader() {
        // Создаем TODO для удаления
        Todo todo = new TodoBuilder().setId(2)
                .setText("Task to Delete")
                .setCompleted(false)
                .build();
        todoRequester.getValidatedRequest()
                .create(todo);
        // Отправляем DELETE запрос без заголовка Authorization
        unAuthTodoRequester.getRequest()
                .delete(todo.getId())
                .then()
                .spec(new IncorrectDataResponse().unAuthorized());
        List<Todo> todos = unAuthTodoRequester.getValidatedRequest()
                .readAll();
        // Проверяем, что TODO не было удалено
        Assertions.assertTrue(todos.contains(todo), "Задача не удалена");
    }

    /**
     * TC3: Попытка удаления TODO с некорректными учетными данными.
     */
    @Test
    public void testDeleteTodoWithInvalidAuth() {
        // Создаем TODO для удаления
        Todo todo = new TodoBuilder().setId(3)
                .setText("Task to Delete")
                .setCompleted(false)
                .build();
        unAuthTodoRequester.getValidatedRequest()
                .create(todo);
        // Отправляем DELETE запрос с некорректной авторизацией
        new TodoRequest(invalidAuthSpec()).delete(todo.getId())
                .then()
                .spec(new IncorrectDataResponse().unAuthorized());
        // Проверяем, что TODO не было удалено
        List<Todo> todos = unAuthTodoRequester.getValidatedRequest()
                .readAll();
        Assertions.assertTrue(todos.contains(todo), "Задача не удалена");
    }

    /**
     * TC4: Удаление TODO с несуществующим id.
     */
    @Test
    public void testDeleteNonExistentTodo() {
        Todo todo = new TodoBuilder().setId(999)
                .setText("Task to Delete")
                .setCompleted(false)
                .build();

        todoRequester.getRequest()
                .delete(todo.getId())
                .then()
                .spec(new IncorrectDataResponse().notFound());
        // В данном случае, поскольку мы не добавляли задач с id 999, список должен быть пуст или содержать только ранее добавленные задачи
    }
}
