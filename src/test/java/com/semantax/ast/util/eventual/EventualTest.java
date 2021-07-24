package com.semantax.ast.util.eventual;

import junit.framework.TestCase;

import static org.junit.Assert.assertThrows;

public class EventualTest extends TestCase {

    public void test_fulfill_throws() {
        Eventual<String> eventual = Eventual.unfulfilled();
        eventual.fulfill("foo");
        assertThrows(FulfilledException.class, () -> eventual.fulfill("bar"));
    }

    public void test_get() {
        Eventual<String> eventual = Eventual.unfulfilled();
        eventual.fulfill("foo");
        assertEquals("foo", eventual.get());
    }

    public void test_get_throws() {
        Eventual<String> eventual = Eventual.unfulfilled();
        assertThrows(UnfulfilledException.class, eventual::get);
    }

    public void test_isFulfilled() {
        Eventual<String> eventual = Eventual.unfulfilled();
        assertFalse(eventual.isFulfilled());
        eventual.fulfill("foo");
        assertTrue(eventual.isFulfilled());
    }

    public void test_toString() {
        Eventual<String> eventual = Eventual.unfulfilled();
        assertTrue(eventual.toString().contains("unfulfilled"));
        eventual.fulfill("foo");
        assertTrue(eventual.toString().contains("foo"));
    }
}