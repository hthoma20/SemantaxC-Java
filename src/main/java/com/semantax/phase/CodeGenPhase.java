package com.semantax.phase;

import com.semantax.ast.node.Program;
import com.semantax.phase.codegen.CodeEmitter;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Process a correct program and return the file paths that were created
 */
public class CodeGenPhase implements Phase<Program, Set<String>> {

    private final CodeEmitter codeEmitter;

    private static final String OUTPUT_FILE = "out.cpp";

    @Inject
    public CodeGenPhase() {
        this.codeEmitter = new CodeEmitter(OUTPUT_FILE);
    }

    @Override
    public Optional<Set<String>> process(Program input) {

        codeEmitter.emitLine("#include \"runtime.h\"");

        return Optional.of(new HashSet<>(Collections.singleton(OUTPUT_FILE)));
    }







}
