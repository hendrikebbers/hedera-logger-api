package com.swirlds.logging.provider.system;

import java.util.Enumeration;
import java.util.ResourceBundle;

public class EmptyResourceBundle extends ResourceBundle {

    private final static EmptyResourceBundle INSTANCE = new EmptyResourceBundle();

    @Override
    protected Object handleGetObject(String key) {
        return null;
    }

    @Override
    public Enumeration<String> getKeys() {
        return new Enumeration<String>() {
            @Override
            public boolean hasMoreElements() {
                return false;
            }

            @Override
            public String nextElement() {
                return null;
            }
        };
    }

    public static EmptyResourceBundle getInstance() {
        return INSTANCE;
    }
}
