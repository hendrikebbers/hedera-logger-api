package com.swirlds.logging.api.internal.configuration;

import com.swirlds.config.api.Configuration;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

public class LogConfiguration implements Configuration {

    private final Map<String, String> values;

    public LogConfiguration() {
        values = new HashMap<>();

        URL configProperties = LogConfiguration.class.getClassLoader().getResource("log.properties");
        if (configProperties != null) {
            try (InputStream inputStream = configProperties.openStream()) {
                Properties properties = new Properties();
                properties.load(inputStream);
                properties.forEach((key, value) -> values.put((String) key, (String) value));
            } catch (Exception e) {
                final System.Logger systemLogger = System.getLogger(LogConfiguration.class.getName());
                systemLogger.log(System.Logger.Level.ERROR, "Can not load logging configuration!", e);
            }
        }
    }

    @Override
    public Stream<String> getPropertyNames() {
        return values.keySet().stream();
    }

    @Override
    public boolean exists(String s) {
        return values.containsKey(s);
    }

    @Override
    public String getValue(String s) throws NoSuchElementException {
        return values.get(s);
    }

    @Override
    public String getValue(String s, String s1) {
        return Optional.ofNullable(values.get(s)).orElse(s1);
    }

    @Override
    public <T> T getValue(String s, Class<T> aClass) throws NoSuchElementException, IllegalArgumentException {
        if (aClass == Boolean.class) {
            return (T) Boolean.valueOf(values.get(s));
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
