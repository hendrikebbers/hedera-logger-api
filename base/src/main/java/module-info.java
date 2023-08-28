module com.swirlds.base {
    exports com.swirlds.base.context;
    exports com.swirlds.base.context.internal to com.swirlds.logging.api;

    requires transitive static com.github.spotbugs.annotations;
}