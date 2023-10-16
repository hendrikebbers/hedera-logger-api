import com.swirlds.logging.api.extensions.handler.LogHandlerFactory;
import com.swirlds.logging.api.extensions.provider.LogProviderFactory;

module com.swirlds.logging.api {
    exports com.swirlds.logging.api;
    exports com.swirlds.logging.api.extensions.provider;
    exports com.swirlds.logging.api.extensions.handler;
    exports com.swirlds.logging.api.extensions.emergency;
    exports com.swirlds.logging.api.extensions.event;

    exports com.swirlds.logging.api.internal.level to com.swirlds.logging.api.testfixture, com.swirlds.logging.handler.synced, com.swirlds.logging.api.test;
    exports com.swirlds.logging.api.internal.emergency to com.swirlds.logging.api.testfixture, com.swirlds.logging.api.test;
    exports com.swirlds.logging.api.internal.format;
    exports com.swirlds.logging.api.internal;
    exports com.swirlds.logging.api.internal.event;

    uses LogHandlerFactory;
    uses LogProviderFactory;

    requires transitive com.swirlds.base;
    requires com.swirlds.config;
}