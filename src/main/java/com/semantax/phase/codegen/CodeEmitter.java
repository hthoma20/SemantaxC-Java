package com.semantax.phase.codegen;

import com.semantax.exception.CompilerException;

import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * Utility class to emit text to a file
 */
public class CodeEmitter {

    private final PrintStream out;

    public CodeEmitter(String filePath) {
        try {
            this.out = new PrintStream(filePath);
        } catch (FileNotFoundException e) {
            throw CompilerException.of("Couldn't open file %s for writing", filePath);
        }
    }

    public void emit(String code, Object... args) {
        out.printf(code, args);
    }

    public void emitLine(String code, Object... args) {
        emit(code + "%n", args);
    }
}
