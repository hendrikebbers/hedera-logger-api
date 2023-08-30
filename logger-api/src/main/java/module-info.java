import com.swirlds.logging.api.extensions.handler.LogHandlerFactory;
import com.swirlds.logging.api.extensions.provider.LogProvider;

module com.swirlds.logging.api {
    exports com.swirlds.logging.api;

    exports com.swirlds.logging.api.extensions to com.swirlds.logging.test.api, com.swirlds.logging.handler.console, com.swirlds.logging.handler.noop, com.swirlds.logging.adapter.jul, com.swirlds.logging.adapter.system, com.swirlds.logging.handler.log4j;
    exports com.swirlds.logging.api.extensions.provider to com.swirlds.logging.adapter.jul, com.swirlds.logging.adapter.system;
    exports com.swirlds.logging.api.extensions.handler to com.swirlds.logging.handler.console, com.swirlds.logging.handler.log4j, com.swirlds.logging.handler.noop;
    exports com.swirlds.logging.api.internal to com.swirlds.logging.test.api;

    uses LogHandlerFactory;
    uses LogProvider;

    requires transitive com.swirlds.base;
    requires com.swirlds.config;
    requires java.logging;
}