package com.semantax.common;

/**
 * Mark that something went wrong with the compile. This indicates a bug in the compiler itself.
 */
public class CompilerException extends RuntimeException {
    public CompilerException(String message) {
        super(message);
    }
}
