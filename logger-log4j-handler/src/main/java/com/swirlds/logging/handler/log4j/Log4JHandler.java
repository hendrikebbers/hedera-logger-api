package com.swirlds.logging.handler.log4j;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.extensions.LogEvent;
import com.swirlds.logging.api.extensions.LogHandler;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

public class Log4JHandler implements LogHandler {

    private final Log4jForwarder forwarder;

    private final Configuration configuration;

    public Log4JHandler(final Configuration configuration) {
        this.configuration = configuration;
        this.forwarder = new Log4jForwarder();
        configureFileLogging();
    }

    @Override
    public String getName() {
        return "Log4J Handler";
    }

    @Override
    public boolean isActive() {
        return configuration.getValue("logging.handler.log4j.enabled", Boolean.class, false);
    }

    @Override
    public void onLogEvent(LogEvent event) {
        forwarder.log(event);
    }

    @Override
    public boolean isEnabled(String name, Level level) {
        return true;
    }

    private static void configureFileLogging() {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        builder.setStatusLevel(org.apache.logging.log4j.Level.TRACE);
        builder.setConfigurationName("loggingConfig");

        builder.add(createFileAppender("file", builder));

        builder.add(builder.newRootLogger(org.apache.logging.log4j.Level.TRACE)
                .add(builder.newAppenderRef("file")));
        Configurator.initialize(builder.build());
    }

    private final static String PATTERN = "%d %c [%t] %-5level: %msg [%marker] %X %n%throwable";

    private static AppenderComponentBuilder createFileAppender(final String name,
            final ConfigurationBuilder<BuiltConfiguration> builder) {
        LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout")
                .addAttribute("pattern", PATTERN);
        return builder.newAppender(name, "File")
                .addAttribute("fileName", "log4j-benchmark.log")
                .addAttribute("append", false)
                .add(layoutBuilder);
    }
}
