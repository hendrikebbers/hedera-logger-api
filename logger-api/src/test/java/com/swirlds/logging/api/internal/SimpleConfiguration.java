package com.swirlds.logging.api.internal;

import com.swirlds.config.api.Configuration;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class SimpleConfiguration implements Configuration {

    private final Map<String, String> properties = new ConcurrentHashMap<>();

    public void setProperty(String name, String value) {
        properties.put(name, value);
    }

    @NonNull
    @Override
    public Stream<String> getPropertyNames() {
        return properties.keySet().stream();
    }

    @Override
    public boolean exists(@NonNull String s) {
        return properties.containsKey(s);
    }

    @Nullable
    @Override
    public String getValue(@NonNull String s) throws NoSuchElementException {
        return properties.get(s);
    }

    @Nullable
    @Override
    public String getValue(@NonNull String s, @Nullable String s1) {
        return Optional.ofNullable(properties.get(s)).orElse(s1);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getValue(@NonNull String s, @NonNull Class<T> aClass)
            throws NoSuchElementException, IllegalArgumentException {
        if (aClass == String.class) {
            return (T) getValue(s);
        }
        if (aClass == Boolean.class) {
            return (T) Boolean.valueOf(getValue(s));
        }
        throw new IllegalStateException("Unsupported type: " + aClass.getName());
    }

    @Nullable
    @Override
    public <T> T getValue(@NonNull String s, @NonNull Class<T> aClass, @Nullable T t) throws IllegalArgumentException {
        return Optional.ofNullable(getValue(s, aClass)).orElse(t);
    }

    @Nullable
    @Override
    public List<String> getValues(@NonNull String s) {
        throw new IllegalStateException("Not supported");
    }

    @Nullable
    @Override
    public List<String> getValues(@NonNull String s, @Nullable List<String> list) {
        throw new IllegalStateException("Not supported");
    }

    @Nullable
    @Override
    public <T> List<T> getValues(@NonNull String s, @NonNull Class<T> aClass)
            throws NoSuchElementException, IllegalArgumentException {
        throw new IllegalStateException("Not supported");
    }

    @Nullable
    @Override
    public <T> List<T> getValues(@NonNull String s, @NonNull Class<T> aClass, @Nullable List<T> list)
            throws IllegalArgumentException {
        throw new IllegalStateException("Not supported");
    }

    @Nullable
    @Override
    public Set<String> getValueSet(@NonNull String s) {
        throw new IllegalStateException("Not supported");
    }

    @Nullable
    @Override
    public Set<String> getValueSet(@NonNull String s, @Nullable Set<String> set) {
        throw new IllegalStateException("Not supported");
    }

    @Nullable
    @Override
    public <T> Set<T> getValueSet(@NonNull String s, @NonNull Class<T> aClass)
            throws NoSuchElementException, IllegalArgumentException {
        throw new IllegalStateException("Not supported");
    }

    @Nullable
    @Override
    public <T> Set<T> getValueSet(@NonNull String s, @NonNull Class<T> aClass, @Nullable Set<T> set)
            throws IllegalArgumentException {
        throw new IllegalStateException("Not supported");
    }

    @NonNull
    @Override
    public <T extends Record> T getConfigData(@NonNull Class<T> aClass) {
        throw new IllegalStateException("Not supported");
    }

    @NonNull
    @Override
    public Collection<Class<? extends Record>> getConfigDataTypes() {
        throw new IllegalStateException("Not supported");
    }
}
