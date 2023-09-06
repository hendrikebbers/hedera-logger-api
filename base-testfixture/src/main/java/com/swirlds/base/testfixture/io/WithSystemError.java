package com.swirlds.base.testfixture.io;

import com.swirlds.base.testfixture.io.internal.SystemErrorExtension;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Isolated;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Isolated
@Inherited
@ExtendWith(SystemErrorExtension.class)
public @interface WithSystemError {
}
