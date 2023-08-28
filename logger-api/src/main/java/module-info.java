import com.swirlds.logging.api.extensions.LogAdapter;
import com.swirlds.logging.api.extensions.LogHandlerFactory;

module com.swirlds.logging.api {
    exports com.swirlds.logging.api;

    exports com.swirlds.logging.api.extensions to com.swirlds.logging.test.api, com.swirlds.logging.handler.console, com.swirlds.logging.handler.noop, com.swirlds.logging.adapter.jul, com.swirlds.logging.adapter.system, com.swirlds.logging.handler.log4j;
    exports com.swirlds.logging.api.internal;

    uses LogHandlerFactory;
    uses LogAdapter;

    requires transitive com.swirlds.base;
    requires com.swirlds.config;
    requires java.logging;
}