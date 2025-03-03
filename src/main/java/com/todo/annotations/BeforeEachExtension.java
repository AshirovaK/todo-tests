package com.todo.annotations;

import com.todo.config.Configuration;
import com.todo.models.TodoBuilder;
import com.todo.requests.TodoRequest;
import com.todo.specs.request.RequestSpec;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;


public class BeforeEachExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        var testMethod = extensionContext.getRequiredTestMethod();

        mobileExecutionExtension(testMethod);
        prepareTodoExtension(testMethod);
    }

    private void mobileExecutionExtension(Method testMethod) {
        var mobile = testMethod.getAnnotation(Mobile.class);
        Configuration.setProperty("version", mobile != null ? "mobile" : "");
    }

    private void prepareTodoExtension(Method testMethod) {
        var prepareTodo = testMethod.getAnnotation(PrepareTodo.class);
        //TODO вынести в метод?
        if (prepareTodo != null) {
            for (int i = 0; i < prepareTodo.value(); i++) {
                new TodoRequest(RequestSpec.authSpec())
                        .create(new TodoBuilder()
                                .setId(Long.parseLong(RandomStringUtils.randomNumeric(3)))
                                .setText("123")
                                .build());
            }
        }
    }
}