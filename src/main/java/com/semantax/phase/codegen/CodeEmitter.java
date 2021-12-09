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
        out.printf(code, args);
    }

    public void beginLine() {
        for (int i = 0; i < indentLevel; i++) {
            out.print(INDENT);
        }
    }

    public void endLine(String code, Object... args) {
        out.printf(code, args);
        out.printf("%n");
    }

    public void emitLine(String code, Object... args) {
        beginLine();
        endLine(code, args);
    }

    public void indent() {
        indentLevel += 1;
    }

    public void unIndent() {
        indentLevel -= 1;
    }
}
