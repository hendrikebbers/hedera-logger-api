package com.swirlds.base.testfixture.io.internal;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class Foo {

    public static void main(String[] args) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        baos.toString();
    }
}
