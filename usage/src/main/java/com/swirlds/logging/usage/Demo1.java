package com.swirlds.logging.usage;

import com.swirlds.logging.api.Logger;
import com.swirlds.logging.api.Loggers;

public class Demo1 {

    private final static Logger logger = Loggers.getLogger(Demo1.class);

    public static void main(String[] args) {
        logger.info("Hello World!");
    }
}
