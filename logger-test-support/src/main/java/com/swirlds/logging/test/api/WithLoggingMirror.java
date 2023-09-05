package com.swirlds.logging.test.api;

import com.swirlds.logging.test.api.internal.LoggerMirrorExtension;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Isolated;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Isolated
@ExtendWith(LoggerMirrorExtension.class)
public @interface WithLoggingMirror {
}
