import com.swirlds.logging.api.extensions.handler.LogHandlerFactory;

module com.swirlds.logging.handler.noop {
    requires com.swirlds.logging.api;
    requires com.swirlds.config;

    provides LogHandlerFactory
            with com.swirlds.logging.handler.noop.NoopHandlerFactory;
}