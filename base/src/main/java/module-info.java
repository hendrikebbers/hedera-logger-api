@SuppressWarnings("requires-transitive-automatic")
module com.swirlds.base {
    exports com.swirlds.base.context;
    exports com.swirlds.base.context.internal to com.swirlds.logging.api, com.swirlds.logging.provider.log4j, com.swirlds.base.context.test;

    requires transitive static com.github.spotbugs.annotations;
}