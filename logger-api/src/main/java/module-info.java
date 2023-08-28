module com.swirlds.logging.api {
    exports com.swirlds.logging.api.internal to com.swirlds.logging.test.api;
    exports com.swirlds.logging.api;

    requires transitive com.swirlds.base;
}