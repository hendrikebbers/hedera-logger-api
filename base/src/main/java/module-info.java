@SuppressWarnings("requires-transitive-automatic")
module com.swirlds.base {
    requires transitive static com.github.spotbugs.annotations;

    exports com.swirlds.base.context;
    exports com.swirlds.base.context.internal to com.swirlds.base.context.test,
            com.swirlds.logging.api,
            com.swirlds.logging.provider.log4j;
}