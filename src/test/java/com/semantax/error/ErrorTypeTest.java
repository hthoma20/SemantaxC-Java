package com.semantax.error;

import junit.framework.TestCase;

import java.util.Arrays;

public class ErrorTypeTest extends TestCase {

    public void test_uniqueIds() {
        long uniqueIds = Arrays.stream(ErrorType.values())
                .map(ErrorType::getId)
                .distinct()
                .count();
        assertEquals("ErrorType ids should be unique", ErrorType.values().length, uniqueIds);
    }
}