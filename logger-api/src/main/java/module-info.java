import com.swirlds.logging.api.extensions.handler.LogHandlerFactory;
import com.swirlds.logging.api.extensions.provider.LogProviderFactory;

module com.swirlds.logging.api {
    exports com.swirlds.logging.api;
    exports com.swirlds.logging.api.extensions;
    exports com.swirlds.logging.api.extensions.provider;
    exports com.swirlds.logging.api.extensions.handler;

    exports com.swirlds.logging.api.internal to com.swirlds.logging.test.api;
    exports com.swirlds.logging.api.internal.util to com.swirlds.logging.test.api;
    exports com.swirlds.logging.api.internal.level to com.swirlds.logging.test.api, com.swirlds.logging.handler.synced;
    exports com.swirlds.logging.api.internal.emergency to com.swirlds.logging.test.api;
    exports com.swirlds.logging.api.internal.format;

    uses LogHandlerFactory;
    uses LogProviderFactory;

    requires transitive com.swirlds.base;
    requires com.swirlds.config;
}