package com.swirlds.logging.api.internal.level;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.extensions.EmergencyLogger;
import com.swirlds.logging.api.extensions.EmergencyLoggerProvider;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class LoggingLevelConfig {

    private final static EmergencyLogger EMERGENCY_LOGGER = EmergencyLoggerProvider.getEmergencyLogger();

    private final static System.Logger LOGGER = System.getLogger(LoggingLevelConfig.class.getName());

    private final static String DEFAULT_PREFIX = "logging.level";

    private final Map<String, Level> levelCache;

    private final Configuration configuration;

    private final List<String> levelConfigProperties;

    private final String prefix;

    private final Level defaultLevel;

    public LoggingLevelConfig(@NonNull Configuration configuration) {
        this(configuration, DEFAULT_PREFIX);
    }


    public LoggingLevelConfig(Configuration configuration, String prefix) {
        this(configuration, prefix, Level.INFO);
    }

    public LoggingLevelConfig(@NonNull Configuration configuration, @NonNull String prefix,
            @NonNull Level defaultLevel) {
        this.configuration = Objects.requireNonNull(configuration, "configuration must not be null");
        this.prefix = Objects.requireNonNull(prefix, "prefix must not be null");
        this.defaultLevel = Objects.requireNonNull(defaultLevel, "defaultLevel must not be null");
        this.levelCache = new ConcurrentHashMap<>();
        this.levelConfigProperties = configuration.getPropertyNames()
                .filter(n -> n.startsWith(this.prefix))
                .map(n -> n.substring(this.prefix.length()))
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
        return levelConfigProperties.stream()
                .filter(n -> name.trim().startsWith(n))
                .reduce((a, b) -> {
                    if (a.length() > b.length()) {
                        return a;
                    } else {
                        return b;
                    }
                })
                .map(n -> {
                    if (n.isBlank()) {
                        return this.prefix;
                    } else {
                        return this.prefix + "." + n;
                    }
                })
                .map(configuration::getValue)
                .map(String::toUpperCase)
                .map(Level::valueOf)
                .orElse(defaultLevel);
    }
}
