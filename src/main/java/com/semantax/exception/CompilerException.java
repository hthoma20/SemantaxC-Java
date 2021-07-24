package com.semantax.exception;

/**
 * Mark that something went wrong with the compile. This indicates a bug in the compiler itself.
 */
public class CompilerException extends RuntimeException {
    public CompilerException(String message) {
        super(message);
    }

    public static CompilerException of(String message, Object... args) {
        return new CompilerException(String.format(message, args));
    }
}
