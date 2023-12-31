module com.swirlds.base.testfixture {
    exports com.swirlds.base.testfixture.context;
    exports com.swirlds.base.testfixture.io;
    exports com.swirlds.base.testfixture.util;
    exports com.swirlds.base.testfixture.concurrent;

    exports com.swirlds.base.testfixture.context.internal to org.junit.platform.commons;
    exports com.swirlds.base.testfixture.io.internal to org.junit.platform.commons;
    exports com.swirlds.base.testfixture.concurrent.internal to org.junit.platform.commons;

    requires org.junit.jupiter.api;
    requires com.swirlds.base;
    requires transitive jakarta.inject;
}