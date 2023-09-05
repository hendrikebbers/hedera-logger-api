package com.swirlds.logging.usage.test;

import com.swirlds.logging.test.api.LoggingMirror;
import com.swirlds.logging.test.api.internal.LoggerTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestService {

    private LoggingMirror mirror;

    @BeforeEach
    void beforeEach() {
        mirror = LoggerTestSupport.createMirror(Service.class);
    }

    @AfterEach
    void afterEach() {
        if (mirror != null) {
            LoggerTestSupport.disposeMirror(mirror);
        }
    }

    @Test
    void test() {
        //given
        Service service = new Service();

        //when
        service.doSomething();

        //then
        Assertions.assertEquals(1, mirror.getEventCount());
    }

}
