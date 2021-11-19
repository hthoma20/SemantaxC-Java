package com.semantax.testutil;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.Optional;
import java.util.function.Predicate;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.argThat;

public class AssertionUtil {

    /**
     * assert that a < b
     *
     * @param a LHS of lt inequality
     * @param b RHS of lt inequality
     */
    public static void assertLessThan(int a, int b) {
        assertTrue(String.format("Expected %d < %d", a, b), a < b);
    }

    public static <T> T matchesPred(Predicate<T> predicate) {
        return argThat(new BaseMatcher<T>() {

            @Override
            public void describeTo(Description description) { }

            @Override
            public boolean matches(Object o) {
                return predicate.test((T) o);
            }
        });
    }
}
