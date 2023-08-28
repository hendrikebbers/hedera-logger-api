import com.swirlds.logging.api.extensions.LogHandlerFactory;

module com.swirlds.logging.handler.noop {
    requires com.swirlds.logging.api;
    requires com.swirlds.config;

    provides LogHandlerFactory
            with com.swirlds.logging.handler.noop.NoopHandlerFactory;
}