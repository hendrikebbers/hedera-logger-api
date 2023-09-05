package com.swirlds.logging.provider.log4j;

import com.swirlds.base.context.Context;
import com.swirlds.base.context.internal.ThreadLocalContext;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.spi.ThreadContextMap;

public class Log4JThreadContextMap implements ThreadContextMap {

    @Override
    public void clear() {
        ThreadLocalContext.getInstance().clear();
    }

    @Override
    public void remove(String key) {
        Context.getThreadLocalContext().remove(key);
    }

    @Override
    public void put(String key, String value) {
        Context.getThreadLocalContext().add(key, value);
    }

    @Override
    public boolean containsKey(String key) {
        return ThreadLocalContext.getContextMap().containsKey(key);
    }

    @Override
    public String get(String key) {
        return ThreadLocalContext.getContextMap().get(key);
    }

    @Override
    public Map<String, String> getCopy() {
        return new AbstractMap<>() {

            @Override
            public Set<Entry<String, String>> entrySet() {
                return new AbstractSet<>() {
                    @Override
                    public Iterator<Entry<String, String>> iterator() {
                        return new Iterator<>() {

                            private final Iterator<Entry<String, String>> iterator = ThreadLocalContext.getContextMap()
                                    .entrySet().iterator();

                            private Entry<String, String> lastEntry;

                            @Override
                            public void remove() {
                                if (lastEntry != null) {
                                    ThreadLocalContext.getInstance().remove(lastEntry.getKey());
                                } else {
                                    throw new IllegalStateException("next() must be called before remove()");
                                }
                            }

                            @Override
                            public boolean hasNext() {
                                return iterator.hasNext();
                            }

                            @Override
                            public Entry<String, String> next() {
                                lastEntry = iterator.next();
                                return lastEntry;
                            }
                        };
                    }

                    @Override
                    public int size() {
                        return ThreadLocalContext.getContextMap().size();
                    }
                };
            }

            @Override
            public String put(String key, String value) {
                String old = ThreadLocalContext.getContextMap().get(key);
                ThreadLocalContext.getInstance().add(key, value);
                return old;
            }
        };
    }

    @Override
    public Map<String, String> getImmutableMapOrNull() {
        return ThreadLocalContext.getContextMap();
    }

    @Override
    public boolean isEmpty() {
        return ThreadLocalContext.getContextMap().isEmpty();
    }

}
