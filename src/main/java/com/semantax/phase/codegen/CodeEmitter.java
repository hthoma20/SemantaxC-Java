package com.semantax.phase.codegen;

import com.semantax.exception.CompilerException;
import lombok.Setter;

import java.io.PrintStream;

/**
 * Utility class to emit text to a file
 */
public class CodeEmitter {

    private static final String INDENT = "\t";

    private final PrintStream out;
    private int indentLevel = 0;

    private boolean annotationsEnabled = true;
    @Setter
    private boolean annotateCaller = false;


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

    public void beginLine(String code, Object... args) {
        for (int i = 0; i < indentLevel; i++) {
            out.print(INDENT);
        }
        emit(code, args);
    }

    public void endLine(String code, Object... args) {
        emit(code, args);

        if (annotateCaller) {
            StackTraceElement caller = getCaller();
            out.printf(" // %s:%d", caller.getClassName(), caller.getLineNumber());
        }

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

    public void annotateLine(String annotation, Object... args) {
        if (!annotationsEnabled) return;
        emitLine(annotation, args);
    }

    public void annotateIndent() {
        if (!annotationsEnabled) return;
        indent();
    }

    public void annotateUnIndent() {
        if (!annotationsEnabled) return;
        unIndent();
    }

    private StackTraceElement getCaller() {
        String className = getClass().getName();
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 1; i < stackTrace.length; i++) {
            if (!stackTrace[i].getClassName().equals(className)) {
                return stackTrace[i];
            }
        }

        throw new CompilerException("Nobody called CodeEmitter");
    }
}
