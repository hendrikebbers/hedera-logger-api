package com.swirlds.base.testfixture.concurrent.internal;

import com.swirlds.base.testfixture.concurrent.TestExecutor;
import com.swirlds.base.testfixture.util.TestInjector;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.lang.reflect.Method;
import java.util.Objects;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

public class TestExecutorExtension implements InvocationInterceptor, ParameterResolver {

    @Override
    public void interceptTestMethod(@NonNull final Invocation<Void> invocation,
            @NonNull final ReflectiveInvocationContext<Method> invocationContext,
            @NonNull final ExtensionContext extensionContext) throws Throwable {
        Objects.requireNonNull(invocation, "invocation must not be null");
        Objects.requireNonNull(extensionContext, "extensionContext must not be null");
        TestInjector.injectInTest(TestExecutor.class, () -> new ConcurrentTestSupport(), extensionContext);
        invocation.proceed();
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return TestInjector.supportsParameter(parameterContext, TestExecutor.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return TestInjector.resolveParameter(parameterContext, () -> new ConcurrentTestSupport());
    }
}