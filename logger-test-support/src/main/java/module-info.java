module com.swirlds.logging.test.api {
    exports com.swirlds.logging.test.api;
    exports com.swirlds.logging.test.api.internal;

    requires transitive com.swirlds.logging.api;
    requires transitive org.junit.jupiter.api;
    requires transitive jakarta.inject;
}