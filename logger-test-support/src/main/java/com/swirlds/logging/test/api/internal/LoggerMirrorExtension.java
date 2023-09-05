package com.swirlds.logging.test.api.internal;

import com.swirlds.logging.test.api.LoggingMirror;
import jakarta.inject.Inject;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

public class LoggerMirrorExtension implements InvocationInterceptor {

    @Override
    public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext,
            ExtensionContext extensionContext) throws Throwable {
        final LoggingMirror loggingMirror = LoggerTestSupport.createMirror();
        try {
            final Class<?> testClass = extensionContext.getRequiredTestClass();
            Arrays.asList(testClass.getDeclaredFields()).stream()
                    .filter(field -> !Modifier.isFinal(field.getModifiers()))
                    .filter(field -> !Modifier.isStatic(field.getModifiers()))
                    .filter(field -> field.isAnnotationPresent(Inject.class))
                    .filter(field -> Objects.equals(field.getType(), LoggingMirror.class))
                    .forEach(field -> {
                        try {
                            field.setAccessible(true);
                            field.set(extensionContext.getRequiredTestInstance(), loggingMirror);
                        } catch (Exception ex) {
                            throw new RuntimeException("Error in injecting mirror", ex);
                        }
                    });
            invocation.proceed();
        } finally {
            loggingMirror.dispose();
        }
    }
}
