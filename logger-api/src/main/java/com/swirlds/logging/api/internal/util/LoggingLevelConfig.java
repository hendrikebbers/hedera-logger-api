package com.swirlds.logging.api.internal.util;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.Level;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class LoggingLevelConfig {

    private final static System.Logger LOGGER = System.getLogger(LoggingLevelConfig.class.getName());

    private final Map<String, Level> levelCache;

    private final Configuration configuration;

    private final List<String> levelConfigProperties;

    public LoggingLevelConfig(@NonNull Configuration configuration) {
        this.configuration = Objects.requireNonNull(configuration, "configuration must not be null");
        this.levelCache = new ConcurrentHashMap<>();
        this.levelConfigProperties = configuration.getPropertyNames()
                .filter(n -> n.startsWith("logging.level"))
                .map(n -> n.substring("logging.level".length()))
                .map(n -> {
                    if (n.startsWith(".")) {
                        return n.substring(1);
                    } else {
                        return n;
                    }
                })
                .sorted()
                .collect(Collectors.toList());
        LOGGER.log(System.Logger.Level.TRACE, "LoggingLevelConfig initialized with {0} properties",
                levelConfigProperties.size());
    }

    public boolean isEnabled(@NonNull String name, @NonNull Level level) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(level, "level must not be null");
        final Level enabledLevel = levelCache.computeIfAbsent(name, n -> getConfiguredLevel(n));
        return enabledLevel.enabledLoggingOfLevel(level);
    }

    @NonNull
    private Level getConfiguredLevel(@NonNull String name) {
        Objects.requireNonNull(name, "name must not be null");
        return levelConfigProperties.stream()
                .filter(name::startsWith)
                .reduce((a, b) -> {
                    if (a.length() > b.length()) {
                        return a;
                    } else {
                        return b;
                    }
                })
                .map(n -> {
                    if (n.isBlank()) {
                        return "logging.level";
                    } else {
                        return "logging.level." + n;
                    }
                })
                .map(n -> configuration.getValue(n))
                .map(String::toUpperCase)
                .map(Level::valueOf)
                .orElse(Level.INFO);
    }
}
