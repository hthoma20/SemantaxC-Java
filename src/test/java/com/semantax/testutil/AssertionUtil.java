package com.semantax.testutil;

import java.util.Optional;

import static junit.framework.TestCase.assertTrue;

public class AssertionUtil {
    /**
     * Return a throwable thrown by the given runnable, or Optional.empty if
     * the runnable didn't throw
     *
     * @param runnable the Runnable to run
     * @return the exception thrown by the runnable
     */
    public static Optional<Throwable> thrown(Runnable runnable) {
        try {
            runnable.run();
        }
        catch (Throwable exc) {
            return Optional.of(exc);
        }

        return Optional.empty();
    }

    /**
     * assert that a < b
     *
     * @param a LHS of lt inequality
     * @param b RHS of lt inequality
     */
    public static void assertLessThan(int a, int b) {
        assertTrue(String.format("Expected %d < %d", a, b), a < b);
    }
}
