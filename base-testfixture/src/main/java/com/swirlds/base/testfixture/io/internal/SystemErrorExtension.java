package com.swirlds.base.testfixture.io.internal;

import com.swirlds.base.testfixture.io.SystemErrProvider;
import edu.umd.cs.findbugs.annotations.NonNull;
import jakarta.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

/**
 * This class is used to inject a {@link SystemErrProvider} instance into a test and run the test in isolation.
 *
 * @see com.swirlds.base.testfixture.io.WithSystemError
 */
public class SystemErrorExtension implements InvocationInterceptor {

    @Override
    public void interceptTestMethod(@NonNull final Invocation<Void> invocation,
            @NonNull final ReflectiveInvocationContext<Method> invocationContext,
            @NonNull final ExtensionContext extensionContext) throws Throwable {
        Objects.requireNonNull(invocation, "invocation must not be null");
        Objects.requireNonNull(extensionContext, "extensionContext must not be null");
        final PrintStream originalSystemErrorPrintStream = System.err;
        try (final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            final SystemIoProvider provider = new SystemIoProvider(byteArrayOutputStream);
            System.setErr(new PrintStream(byteArrayOutputStream));
            final Class<?> testClass = extensionContext.getRequiredTestClass();
            Arrays.asList(testClass.getDeclaredFields()).stream()
                    .filter(field -> !Modifier.isFinal(field.getModifiers()))
                    .filter(field -> !Modifier.isStatic(field.getModifiers()))
                    .filter(field -> field.isAnnotationPresent(Inject.class))
                    .filter(field -> Objects.equals(field.getType(), SystemErrProvider.class))
                    .forEach(field -> {
                        try {
                            field.setAccessible(true);
                            field.set(extensionContext.getRequiredTestInstance(), provider);
                        } catch (Exception ex) {
                            throw new RuntimeException("Error in injecting mirror", ex);
                        }
                    });
            invocation.proceed();
        } finally {
            System.setErr(originalSystemErrorPrintStream);
        }

    }
}
