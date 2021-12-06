package com.semantax.phase.codegen;

import java.io.PrintStream;

/**
 * Utility class to emit text to a file
 */
public class CodeEmitter {

    private static final String INDENT = "\t";

    private final PrintStream out;
    private int indentLevel = 0;


    public CodeEmitter(PrintStream out) {
        this.out = out;
    }

    public void emit(String code, Object... args) {
        for (int i = 0; i < indentLevel; i++) {
            out.print(INDENT);
        }
        out.printf(code, args);
    }

    public void emitLine(String code, Object... args) {
        emit(code + "%n", args);
    }

    public void indent() {
        indentLevel += 1;
    }

    public void unIndent() {
        indentLevel -= 1;
    }
}
