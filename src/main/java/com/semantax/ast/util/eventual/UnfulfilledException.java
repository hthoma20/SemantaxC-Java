package com.semantax.ast.util.eventual;

import com.semantax.exception.CompilerException;

/**
 * Represents that an Eventual is not yet fulfilled, but some operation
 * was done that assumes it is
 */
public class UnfulfilledException extends CompilerException {
    public UnfulfilledException(String message) {
        super(message);
    }

    public static UnfulfilledException of(String message, Object... args) {
        return new UnfulfilledException(String.format(message, args));
    }
}
