package com.swirlds.logging.handler.rollingfile;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.event.LogEvent;
import com.swirlds.logging.api.extensions.handler.LogHandler;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.layout.PatternLayout;

public class Log4JHandler implements LogHandler {

    private final static String PATTERN = "%d %c [%t] %-5level: %msg [%marker] %X %n%throwable";

    private final Configuration configuration;

    private final RollingFileAppender appender;


    public Log4JHandler(final Configuration configuration) {
        this.configuration = configuration;

        final DefaultRolloverStrategy rolloverStrategy = DefaultRolloverStrategy.newBuilder()
                .withMax("10")
                .build();

        final PatternLayout layout = PatternLayout.newBuilder()
                .withPattern(PATTERN)
                .build();

        appender = RollingFileAppender.newBuilder()
                .setName("file")
                .withFileName("log4j2.log")
                .withFilePattern("log4j2-%d{MM-dd-yy-HH-mm-ss}-%i.log.gz")
                .setLayout(layout)
                .withAppend(true)
                .withBufferSize(4000)
                .withPolicy(SizeBasedTriggeringPolicy.createPolicy(null))
                .withStrategy(rolloverStrategy)
                .build();
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
    public void accept(LogEvent event) {
        appender.append(new Log4JWrappedLogEvent(event));
    }

}
