package com.semantax.ast.util.eventual;

import com.semantax.exception.CompilerException;

/**
 * Represents that an Eventual is already fulfilled, but some operation
 * was done that assumes it is not (such as trying to fulfill it again)
 */
public class FulfilledException extends CompilerException {
    public FulfilledException(String message) {
        super(message);
    }

    public static FulfilledException of(String message, Object... args) {
        return new FulfilledException(String.format(message, args));
    }
}
