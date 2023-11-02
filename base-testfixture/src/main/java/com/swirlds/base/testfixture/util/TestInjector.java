package com.swirlds.base.testfixture.util;

import com.swirlds.base.testfixture.concurrent.TestExecutor;
import edu.umd.cs.findbugs.annotations.NonNull;
import jakarta.inject.Inject;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

/**
 * This class is used to inject a dependency into a test. It should be used by JUnit extensions.
 */
public class TestInjector {

    /**
     * This class should not be instantiated.
     */
    private TestInjector() {
    }

    /**
     * Injects the result of the given supplier into the test instance. The result of the supplier is injected into any
     * field of the test instance that is annotated with {@link Inject} and is of the given type.
     *
     * @param type             the type of the dependency to inject
     * @param supplier         the supplier that provides the dependency
     * @param extensionContext the extension context of the test
     * @param <T>              the type of the dependency to inject
     */
    public static <T> void injectInTest(Class<T> type, Supplier<T> supplier, ExtensionContext extensionContext) {
        final Class<?> testClass = extensionContext.getRequiredTestClass();
        Arrays.asList(testClass.getDeclaredFields()).stream()
                .filter(field -> !Modifier.isFinal(field.getModifiers()))
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .filter(field -> Objects.equals(field.getType(), type))
                .forEach(field -> {
                    try {
                        field.setAccessible(true);
                        field.set(extensionContext.getRequiredTestInstance(), supplier.get());
                    } catch (Exception ex) {
                        throw new RuntimeException("Error in injection", ex);
                    }
                });
    }

    public static <T> boolean supportsParameter(final @NonNull ParameterContext parameterContext,
            final @NonNull Class<T> cls) {
        Objects.requireNonNull(parameterContext, "parameterContext must not be null");
        Objects.requireNonNull(cls, "cls must not be null");
        return Optional.ofNullable(parameterContext)
                .map(c -> c.getParameter())
                .map(p -> p.getType())
                .filter(t -> cls.isAssignableFrom(t))
                .isPresent();
    }

    public static <T> T resolveParameter(final @NonNull ParameterContext parameterContext,
            final @NonNull Supplier<T> instanceSupplier) {
        Objects.requireNonNull(parameterContext, "parameterContext must not be null");
        Objects.requireNonNull(instanceSupplier, "instanceSupplier must not be null");
        return Optional.ofNullable(parameterContext)
                .map(c -> c.getParameter())
                .map(p -> p.getType())
                .filter(t -> t.equals(TestExecutor.class))
                .map(t -> instanceSupplier.get())
                .orElseThrow(() -> new ParameterResolutionException("Could not resolve parameter"));
    }
}
