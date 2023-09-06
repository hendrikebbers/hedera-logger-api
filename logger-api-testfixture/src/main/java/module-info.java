module com.swirlds.logging.api.testfixture {
    exports com.swirlds.logging.api.testfixture;
    exports com.swirlds.logging.api.testfixture.internal to org.junit.platform.commons;

    requires transitive com.swirlds.logging.api;
    requires transitive org.junit.jupiter.api;
    requires transitive jakarta.inject;
}