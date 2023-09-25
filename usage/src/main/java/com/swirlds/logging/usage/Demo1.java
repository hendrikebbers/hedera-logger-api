package com.swirlds.logging.usage;

import com.swirlds.base.context.Context;
import com.swirlds.logging.api.Logger;
import com.swirlds.logging.api.Loggers;

public class Demo1 {

    private final static Logger logger = Loggers.getLogger(Demo1.class);

    public static void main(String[] args) {
        Context.getGlobalContext().add("app", "demo1");
        logger.error("Hello World!");
    }
}
