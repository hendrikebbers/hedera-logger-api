package com.swirlds.logging.api.internal.configuration;

import com.swirlds.config.api.Configuration;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

/**
 * The Logging configuration that is used to configure the logging system. The config is based on a file that is by
 * default in of the application root folder and named "log.properties". The system environment variable
 * "LOG_CONFIG_PATH" can be defined to overwrite the path to the configuration file. The class implements the
 * {@link Configuration} interface since it will later be replaced by using the "real" configuration system.
 */
public class LogConfiguration implements Configuration {

    /**
     * The configuration properties
     */
    private final Map<String, String> properties;

    /**
     * Creates a new instance of the logging configuration.
     */
    public LogConfiguration() {
        properties = new HashMap<>();
        final String logConfigPath = System.getenv("LOG_CONFIG_PATH");
        final URL configProperties = Optional.ofNullable(logConfigPath)
                .map(path -> Path.of(path))
                .map(path -> path.toUri())
                .map(uri -> {
                    try {
                        return uri.toURL();
                    } catch (final Exception e) {
                        throw new RuntimeException("Can not convert path to URL!", e);
                    }
                }).orElseGet(() -> LogConfiguration.class.getClassLoader().getResource("log.properties"));
        if (configProperties != null) {
            try (final InputStream inputStream = configProperties.openStream()) {
                final Properties properties = new Properties();
                properties.load(inputStream);
                properties.forEach((key, value) -> this.properties.put((String) key, (String) value));
            } catch (final Exception e) {
                final System.Logger systemLogger = System.getLogger(LogConfiguration.class.getName());
                systemLogger.log(System.Logger.Level.ERROR, "Can not load logging configuration!", e);
            }
        }
    }

    @Override
    public Stream<String> getPropertyNames() {
        return properties.keySet().stream();
    }

    @Override
    public boolean exists(String s) {
        return properties.containsKey(s);
    }

    @Override
    public String getValue(String s) throws NoSuchElementException {
        return properties.get(s);
    }

    @Override
    public String getValue(String s, String s1) {
        return Optional.ofNullable(properties.get(s)).orElse(s1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getValue(String s, Class<T> aClass) throws NoSuchElementException, IllegalArgumentException {
        if (aClass == Boolean.class) {
            return (T) Boolean.valueOf(properties.get(s));
        }
        throw new IllegalStateException("Unsupported type: " + aClass.getName());
    }

    @Override
    public <T> T getValue(String s, Class<T> aClass, T t) throws IllegalArgumentException {
        return Optional.ofNullable(getValue(s, aClass)).orElse(t);
    }

    @Override
    public List<String> getValues(String s) {
        throw new IllegalStateException("Collections not supported");
    }

    @Override
    public List<String> getValues(String s, List<String> list) {
        throw new IllegalStateException("Collections not supported");
    }

    @Override
    public <T> List<T> getValues(String s, Class<T> aClass) throws NoSuchElementException, IllegalArgumentException {
        throw new IllegalStateException("Collections not supported");
    }

    @Override
    public <T> List<T> getValues(String s, Class<T> aClass, List<T> list) throws IllegalArgumentException {
        throw new IllegalStateException("Collections not supported");
    }

    @Override
    public Set<String> getValueSet(String s) {
        throw new IllegalStateException("Collections not supported");
    }

    @Override
    public Set<String> getValueSet(String s, Set<String> set) {
        throw new IllegalStateException("Collections not supported");
    }

    @Override
    public <T> Set<T> getValueSet(String s, Class<T> aClass) throws NoSuchElementException, IllegalArgumentException {
        throw new IllegalStateException("Collections not supported");
    }

    @Override
    public <T> Set<T> getValueSet(String s, Class<T> aClass, Set<T> set) throws IllegalArgumentException {
        throw new IllegalStateException("Collections not supported");
    }

    @Override
    public <T extends Record> T getConfigData(Class<T> aClass) {
        throw new IllegalStateException("Records not supported");
    }

    @Override
    public Collection<Class<? extends Record>> getConfigDataTypes() {
        throw new IllegalStateException("Records not supported");
    }
}
