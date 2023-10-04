package com.swirlds.logging.api.internal.level;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.extensions.emergency.EmergencyLogger;
import com.swirlds.logging.api.extensions.emergency.EmergencyLoggerProvider;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class LoggingLevelConfig {

    private final static EmergencyLogger EMERGENCY_LOGGER = EmergencyLoggerProvider.getEmergencyLogger();

    private final static String DEFAULT_PREFIX = "logging.level";

    private final Map<String, Level> levelCache;

    private final Map<String, String> levelConfigProperties;

    private final String prefix;

    private final Level defaultLevel;

    public LoggingLevelConfig(@NonNull Configuration configuration) {
        this(configuration, DEFAULT_PREFIX);
    }


    public LoggingLevelConfig(Configuration configuration, String prefix) {
        this(configuration, prefix, Level.INFO);
    }

    public LoggingLevelConfig(Configuration configuration, Level level) {
        this(configuration, DEFAULT_PREFIX, level);
    }

    public LoggingLevelConfig(@NonNull Configuration configuration, @NonNull String prefix,
            @NonNull Level defaultLevel) {
        this.prefix = Objects.requireNonNull(prefix, "prefix must not be null");
        this.defaultLevel = Objects.requireNonNull(defaultLevel, "defaultLevel must not be null");
        this.levelCache = new ConcurrentHashMap<>();
        this.levelConfigProperties = new ConcurrentHashMap<>();
        update(configuration);
    }

    public void update(final @NonNull Configuration configuration) {
        Objects.requireNonNull(configuration, "configuration must not be null");
        levelConfigProperties.clear();
        configuration.getPropertyNames()
                .filter(n -> n.startsWith(this.prefix))
                .forEach(configPropertyName -> {
                    final String name = configPropertyName.substring(this.prefix.length());
                    if (name.startsWith(".")) {
                        levelConfigProperties.put(name.substring(1), configuration.getValue(configPropertyName));
                    } else {
                        levelConfigProperties.put(name, configuration.getValue(configPropertyName));
                    }
                });
        levelCache.clear();
    }

    public boolean isEnabled(@NonNull String name, @NonNull Level level) {
        if (level == null) {
            EMERGENCY_LOGGER.logNPE("level");
            return true;
        }
        if (name == null) {
            EMERGENCY_LOGGER.logNPE("name");
            return true;
        }
        final Level enabledLevel = levelCache.computeIfAbsent(name.trim(), this::getConfiguredLevel);
        return enabledLevel.enabledLoggingOfLevel(level);
    }

    @NonNull
    private Level getConfiguredLevel(@NonNull String name) {
        return levelConfigProperties.keySet().stream()
                .filter(n -> name.trim().startsWith(n))
                .reduce((a, b) -> {
                    if (a.length() > b.length()) {
                        return a;
                    } else {
                        return b;
                    }
                })
                .map(levelConfigProperties::get)
                .map(String::toUpperCase)
                .map(n -> Level.valueOfOrElse(n, defaultLevel))
                .orElse(defaultLevel);
    }
}
