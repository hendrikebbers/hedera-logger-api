package com.swirlds.logging.usage.test;

import com.swirlds.logging.api.testfixture.LoggingMirror;
import com.swirlds.logging.api.testfixture.WithLoggingMirror;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@WithLoggingMirror
public class TestService {

    @Inject
    private LoggingMirror mirror;

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
