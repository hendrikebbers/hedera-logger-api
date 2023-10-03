package com.swirlds.base.testfixture.inject;

import jakarta.inject.Inject;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import org.junit.jupiter.api.extension.ExtensionContext;

public class TestInjector {

    private TestInjector() {
    }

    public static <T> void injectInTest(Class<T> type, T instance, ExtensionContext extensionContext) {
        final Class<?> testClass = extensionContext.getRequiredTestClass();
        Arrays.asList(testClass.getDeclaredFields()).stream()
                .filter(field -> !Modifier.isFinal(field.getModifiers()))
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .filter(field -> Objects.equals(field.getType(), type))
                .forEach(field -> {
                    try {
                        field.setAccessible(true);
                        field.set(extensionContext.getRequiredTestInstance(), instance);
                    } catch (Exception ex) {
                        throw new RuntimeException("Error in injection", ex);
                    }
                });
    }
}
