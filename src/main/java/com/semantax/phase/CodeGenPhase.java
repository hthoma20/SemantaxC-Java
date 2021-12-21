package com.semantax.phase;

import com.semantax.ast.node.Program;
import com.semantax.exception.CompilerException;
import com.semantax.phase.codegen.CodeEmitter;
import com.semantax.phase.codegen.FunctionCodeGenerator;
import com.semantax.phase.codegen.GeneratedFunctionRegistry;
import com.semantax.phase.codegen.GeneratedNameRegistry;
import com.semantax.phase.codegen.GeneratedTypeAggregator;
import com.semantax.phase.codegen.GeneratedTypeRegistry;
import com.semantax.phase.codegen.GeneratedVariableRegistry;
import com.semantax.phase.codegen.GlobalVariableCodeGenerator;
import com.semantax.phase.codegen.MainCodeGenerator;
import com.semantax.phase.codegen.RecordCodeGenerator;
import com.semantax.phase.codegen.VariableScopeAnnotator;
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
    private final VariableScopeAnnotator variableScopeAnnotator;
    private final RecordCodeGenerator recordCodeGenerator;
    private final GlobalVariableCodeGenerator variableCodeGenerator;
    private final FunctionCodeGenerator functionCodeGenerator;
    private final MainCodeGenerator mainCodeGenerator;

    @Inject
    public CodeGenPhase(GeneratedTypeAggregator generatedTypeAggregator,
                        VariableScopeAnnotator variableScopeAnnotator,
                        RecordCodeGenerator recordCodeGenerator,
                        GlobalVariableCodeGenerator variableCodeGenerator,
                        FunctionCodeGenerator functionCodeGenerator,
                        MainCodeGenerator mainCodeGenerator) {
        this.generatedTypeAggregator = generatedTypeAggregator;
        this.variableScopeAnnotator = variableScopeAnnotator;
        this.recordCodeGenerator = recordCodeGenerator;
        this.variableCodeGenerator = variableCodeGenerator;
        this.functionCodeGenerator = functionCodeGenerator;
        this.mainCodeGenerator = mainCodeGenerator;
    }

    @Override
    public Optional<Set<String>> process(CodeGenArgs args) {
        CodeEmitter codeEmitter = getCodeEmitter(args.outputPath);

        codeEmitter.emitLine("#include \"runtime.h\"");
        codeEmitter.emitLine("");

        GeneratedTypeRegistry typeRegistry = generatedTypeAggregator.aggregateTypeNames(args.program);
        GeneratedNameRegistry nameRegistry = GeneratedNameRegistry.builder()
                .typeRegistry(typeRegistry)
                .variableRegistry(new GeneratedVariableRegistry())
                .functionRegistry(new GeneratedFunctionRegistry())
                .build();

        variableScopeAnnotator.annotateVariables(args.program);

        recordCodeGenerator.generateTypes(codeEmitter, typeRegistry);
        codeEmitter.emitLine("");
        variableCodeGenerator.generateGlobalVariables(codeEmitter, nameRegistry, args.program);
        codeEmitter.emitLine("");
        functionCodeGenerator.generateFunctions(codeEmitter, nameRegistry, args.program);
        codeEmitter.emitLine("");
        mainCodeGenerator.generateMain(codeEmitter, nameRegistry, args.program);

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
