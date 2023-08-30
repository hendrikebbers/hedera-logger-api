module com.swirlds.base {
    exports com.swirlds.base.context;
    exports com.swirlds.base.context.internal to com.swirlds.logging.api, com.swirlds.logging.adapter.log4j;

    requires transitive static com.github.spotbugs.annotations;
}