package com.semantax.ast.util.eventual;

/**
 * Represents that an Eventual is not yet fulfilled, but some operation
 * was done that assumes it is
 */
public class UnfulfilledException extends RuntimeException {
    public UnfulfilledException(String message) {
        super(message);
    }

    public static UnfulfilledException of(String message, Object... args) {
        return new UnfulfilledException(String.format(message, args));
    }
}
