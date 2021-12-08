package com.semantax.phase;

import com.semantax.ast.node.Program;
import com.semantax.exception.CompilerException;
import com.semantax.phase.codegen.CodeEmitter;
import com.semantax.phase.codegen.GeneratedTypeAggregator;
import com.semantax.phase.codegen.GeneratedTypeRegistry;
import com.semantax.phase.codegen.MainCodeGenerator;
import com.semantax.phase.codegen.RecordCodeGenerator;
import lombok.Builder;

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
public class CodeGenPhase implements Phase<CodeGenPhase.CodeGenArgs, Set<String>> {

    private final GeneratedTypeAggregator generatedTypeAggregator;
    private final RecordCodeGenerator recordCodeGenerator;
    private final MainCodeGenerator mainCodeGenerator;

    @Inject
    public CodeGenPhase(GeneratedTypeAggregator generatedTypeAggregator,
                        RecordCodeGenerator recordCodeGenerator,
                        MainCodeGenerator mainCodeGenerator) {
        this.generatedTypeAggregator = generatedTypeAggregator;
        this.recordCodeGenerator = recordCodeGenerator;
        this.mainCodeGenerator = mainCodeGenerator;
    }

    @Override
    public Optional<Set<String>> process(CodeGenArgs args) {
        CodeEmitter codeEmitter = getCodeEmitter(args.outputPath);

        codeEmitter.emitLine("#include \"runtime.h\"");
        codeEmitter.emitLine("");

        GeneratedTypeRegistry typeRegistry = generatedTypeAggregator.aggregateTypeNames(args.program);
        recordCodeGenerator.generateTypes(codeEmitter, typeRegistry);
        mainCodeGenerator.generateMain(codeEmitter);

        return Optional.of(new HashSet<>(Collections.singleton(args.outputPath)));
    }

    private CodeEmitter getCodeEmitter(String outputFile) {
        try {
            return new CodeEmitter(new PrintStream(outputFile));
        } catch (FileNotFoundException e) {
            throw CompilerException.of("Couldn't construct PrintStream for %s", outputFile);
        }
    }

    @Builder(builderClassName = "Builder")
    public static class CodeGenArgs {
        private final String outputPath;
        private final Program program;
    }

}
