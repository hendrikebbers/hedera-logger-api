module com.swirlds.logging.handler.log4j {
    requires com.swirlds.logging.api;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires com.swirlds.config;
    requires java.scripting; //Otherwise log4j throws an exception at startup

    provides com.swirlds.logging.api.extensions.LogHandlerFactory with com.swirlds.logging.handler.log4j.Log4JHandlerFactory;
}