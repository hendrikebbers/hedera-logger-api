package com.swirlds.base.testfixture.io;

import java.util.stream.Stream;

public interface SystemErrProvider {

    Stream<String> getLines();
}
