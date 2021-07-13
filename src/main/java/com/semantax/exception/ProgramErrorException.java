package com.semantax.exception;

/**
 * Thrown when there is some error in the program to compile
 */
public class ProgramErrorException extends RuntimeException {
    public ProgramErrorException(Throwable cause) {
        super(cause);
    }
}
