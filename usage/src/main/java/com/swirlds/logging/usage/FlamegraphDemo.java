package com.swirlds.logging.usage;

public class FlamegraphDemo {

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 1000; i++) {
            Demo2.main(args);
        }
    }
}
