import com.swirlds.logging.api.extensions.handler.LogHandlerFactory;
import com.swirlds.logging.api.extensions.provider.LogProviderFactory;

module com.swirlds.logging.api {
    exports com.swirlds.logging.api;

    exports com.swirlds.logging.api.extensions to com.swirlds.logging.test.api, com.swirlds.logging.handler.console, com.swirlds.logging.handler.noop, com.swirlds.logging.provider.jul, com.swirlds.logging.provider.system, com.swirlds.logging.handler.log4j, com.swirlds.logging.provider.log4j, com.swirlds.logging.handler.rollingfile, com.swirlds.logging.format, com.swirlds.logging.handler.synced;
    exports com.swirlds.logging.api.extensions.provider to com.swirlds.logging.provider.jul, com.swirlds.logging.provider.system, com.swirlds.logging.provider.log4j;
    exports com.swirlds.logging.api.extensions.handler to com.swirlds.logging.handler.console, com.swirlds.logging.handler.log4j, com.swirlds.logging.handler.noop, com.swirlds.logging.handler.rollingfile, com.swirlds.logging.handler.synced;
    exports com.swirlds.logging.api.internal to com.swirlds.logging.test.api;
    exports com.swirlds.logging.api.internal.util to com.swirlds.logging.test.api;

    uses LogHandlerFactory;
    uses LogProviderFactory;

    requires transitive com.swirlds.base;
    requires com.swirlds.config;
}