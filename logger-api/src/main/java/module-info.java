import com.swirlds.logging.api.extensions.handler.LogHandlerFactory;
import com.swirlds.logging.api.extensions.shipper.LogShipperFactory;

module com.swirlds.logging.api {
    exports com.swirlds.logging.api;

    exports com.swirlds.logging.api.extensions to com.swirlds.logging.test.api, com.swirlds.logging.handler.console, com.swirlds.logging.handler.noop, com.swirlds.logging.shipper.jul, com.swirlds.logging.shipper.system, com.swirlds.logging.handler.log4j;
    exports com.swirlds.logging.api.extensions.shipper to com.swirlds.logging.shipper.jul, com.swirlds.logging.shipper.system;
    exports com.swirlds.logging.api.extensions.handler to com.swirlds.logging.handler.console, com.swirlds.logging.handler.log4j, com.swirlds.logging.handler.noop;
    exports com.swirlds.logging.api.internal to com.swirlds.logging.test.api;
    exports com.swirlds.logging.api.internal.util to com.swirlds.logging.test.api;

    uses LogHandlerFactory;
    uses LogShipperFactory;

    requires transitive com.swirlds.base;
    requires com.swirlds.config;
    requires java.logging;
}