module com.swirlds.logging.handler.loki {
    requires com.swirlds.logging.api;
    requires com.swirlds.config;
    requires com.google.gson;
    requires java.net.http;

    provides com.swirlds.logging.api.extensions.handler.LogHandlerFactory
            with com.swirlds.logging.handler.loki.LokiHandlerFactory;
}