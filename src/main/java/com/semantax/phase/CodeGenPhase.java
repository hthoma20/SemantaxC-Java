package com.semantax.phase;

import com.semantax.ast.node.Program;
import com.semantax.exception.CompilerException;
import com.semantax.phase.codegen.CodeEmitter;
import com.semantax.phase.codegen.GeneratedTypeAggregator;
import com.semantax.phase.codegen.GeneratedTypeRegistry;
import com.semantax.phase.codegen.RecordCodeGenerator;

import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Process a correct program and return the file paths that were created
 */
public class CodeGenPhase implements Phase<Program, Set<String>> {

    private final GeneratedTypeAggregator generatedTypeAggregator;

    private final CodeEmitter codeEmitter;

    private static final String OUTPUT_FILE = "out.cpp";

    @Inject
    public CodeGenPhase(GeneratedTypeAggregator generatedTypeAggregator) {
        this.generatedTypeAggregator = generatedTypeAggregator;
        this.codeEmitter = new CodeEmitter(getPrintStream());
    }

    @Override
    public Optional<Set<String>> process(Program input) {

        codeEmitter.emitLine("#include \"runtime.h\"");

        codeEmitter.emitLine("");

        GeneratedTypeRegistry typeRegistry = generatedTypeAggregator.aggregateTypeNames(input);
        new RecordCodeGenerator(codeEmitter).generateTypes(typeRegistry);

        return Optional.of(new HashSet<>(Collections.singleton(OUTPUT_FILE)));
    }

    private PrintStream getPrintStream() {
        try {
            return new PrintStream(OUTPUT_FILE);
        } catch (FileNotFoundException e) {
            throw CompilerException.of("Couldn't construct PrintStream for %s", OUTPUT_FILE);
        }
    }




}
