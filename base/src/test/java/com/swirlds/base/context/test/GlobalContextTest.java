package com.swirlds.base.context.test;

import com.swirlds.base.context.internal.GlobalContext;
import com.swirlds.base.context.internal.ThreadLocalContext;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GlobalContextTest {

    @BeforeEach
    void setUp() {
        GlobalContext.getInstance().clear();
        ThreadLocalContext.getInstance().clear();
    }

    @AfterEach
    void tearDown() {
        GlobalContext.getInstance().clear();
        ThreadLocalContext.getInstance().clear();
    }

    @Test
    void testNullName() {
        //given
        GlobalContext context = GlobalContext.getInstance();

        //then
        Assertions.assertThrows(NullPointerException.class, () -> context.add(null, "value"));
        Assertions.assertThrows(NullPointerException.class, () -> context.add(null, 1));
        Assertions.assertThrows(NullPointerException.class, () -> context.add(null, 1L));
        Assertions.assertThrows(NullPointerException.class, () -> context.add(null, 1.0D));
        Assertions.assertThrows(NullPointerException.class, () -> context.add(null, 1.0F));
        Assertions.assertThrows(NullPointerException.class, () -> context.add(null, true));
    }

    @Test
    void testAllPut() {
        //given
        GlobalContext context = GlobalContext.getInstance();

        //when
        context.add("key-string", "value");
        context.add("key-int", 1);
        context.add("key-long", 1L);
        context.add("key-double", 1.0D);
        context.add("key-float", 1.0F);
        context.add("key-boolean", true);

        //then
        final Map<String, String> contextMap = context.getContextMap();
        Assertions.assertEquals(6, contextMap.size());
        Assertions.assertEquals("value", contextMap.get("key-string"));
        Assertions.assertEquals("1", contextMap.get("key-int"));
        Assertions.assertEquals("1", contextMap.get("key-long"));
        Assertions.assertEquals("1.0", contextMap.get("key-double"));
        Assertions.assertEquals("1.0", contextMap.get("key-float"));
        Assertions.assertEquals("true", contextMap.get("key-boolean"));
    }

    @Test
    void testAllPutNull() {
        //given
        GlobalContext context = GlobalContext.getInstance();

        //when
        context.add("key-string", null);

        //then
        final Map<String, String> contextMap = context.getContextMap();
        Assertions.assertEquals(1, contextMap.size());
        Assertions.assertEquals(null, contextMap.get("key-string"));
    }

    @Test
    void testOverwrite() {
        //given
        GlobalContext context = GlobalContext.getInstance();

        //when
        context.add("key", "a");
        context.add("key", "b");
        context.add("key", "c");

        //then
        final Map<String, String> contextMap = context.getContextMap();
        Assertions.assertEquals(1, contextMap.size());
        Assertions.assertEquals("c", contextMap.get("key"));
    }

    @Test
    void testRemove() {
        //given
        GlobalContext context = GlobalContext.getInstance();

        //when
        context.add("key", "a");
        context.remove("key");

        //then
        final Map<String, String> contextMap = context.getContextMap();
        Assertions.assertEquals(0, contextMap.size());
    }

    @Test
    void testRemoveNullKey() {
        //given
        GlobalContext context = GlobalContext.getInstance();

        //then
        Assertions.assertThrows(NullPointerException.class, () -> context.remove(null));
    }


    @Test
    void testClear() {
        //given
        GlobalContext context = GlobalContext.getInstance();
        context.add("key", "a");
        context.add("key-2", "a");

        //when
        context.clear();

        //then
        final Map<String, String> contextMap = context.getContextMap();
        Assertions.assertEquals(0, contextMap.size());
    }

    @Test
    void testAutocloseable() {
        //given
        GlobalContext context = GlobalContext.getInstance();
        AutoCloseable closeable = context.add("key", "a");

        //when
        Assertions.assertDoesNotThrow(() -> closeable.close());

        //then
        final Map<String, String> contextMap = context.getContextMap();
        Assertions.assertEquals(0, contextMap.size());
    }
}
