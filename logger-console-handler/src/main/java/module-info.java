import com.swirlds.logging.api.extensions.handler.LogHandlerFactory;

module com.swirlds.logging.handler.console {
    requires com.swirlds.logging.api;
    requires com.swirlds.config;
    requires com.swirlds.logging.format;
    requires com.swirlds.logging.handler.synced;

    provides LogHandlerFactory with com.swirlds.logging.handler.console.ConsoleHandlerFactory;
}