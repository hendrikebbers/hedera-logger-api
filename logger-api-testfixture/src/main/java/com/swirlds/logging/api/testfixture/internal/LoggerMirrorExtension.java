package com.swirlds.logging.api.testfixture.internal;

import com.swirlds.base.testfixture.util.TestInjector;
import com.swirlds.logging.api.testfixture.LoggingMirror;
import java.lang.reflect.Method;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

public class LoggerMirrorExtension implements InvocationInterceptor {

    @Override
    public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext,
            ExtensionContext extensionContext) throws Throwable {
        try (final LoggingMirror loggingMirror = new LoggingMirrorImpl()) {
            TestInjector.injectInTest(LoggingMirror.class, () -> loggingMirror, extensionContext);
            invocation.proceed();
        }
    }
}
