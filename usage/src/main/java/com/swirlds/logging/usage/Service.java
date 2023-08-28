package com.swirlds.logging.usage;

import com.swirlds.logging.api.Logger;
import com.swirlds.logging.api.Loggers;

/**
 * This class is used in the unit tests
 */
public class Service {

    private final static Logger logger = Loggers.getLogger(Service.class);

    public void doSomething() {
        logger.info("Hello World!");
    }
}
