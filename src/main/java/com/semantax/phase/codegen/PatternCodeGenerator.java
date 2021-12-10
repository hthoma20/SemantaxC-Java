package com.semantax.phase.codegen;

import com.semantax.ast.node.Module;
import com.semantax.ast.node.Program;
import com.semantax.ast.node.literal.type.TypeLit;
import com.semantax.ast.node.pattern.PatternDefinition;

import javax.inject.Inject;

/**
 * Generate code for the main function
 */
public class PatternCodeGenerator {

    @Inject
    public PatternCodeGenerator() { }

    public void generatePatterns(CodeEmitter emitter,
                                 GeneratedPatternRegistry patternRegistry,
                                 GeneratedTypeRegistry typeRegistry,
                                 Program program) {
        for (Module module : program.getModules()) {
            for (PatternDefinition pattern : module.getPatterns()) {
                generatePattern(emitter, pattern, typeRegistry, patternRegistry.getPatternName(pattern));
                emitter.emitLine("");
            }
        }
    }

    private void generatePattern(CodeEmitter emitter,
                                 PatternDefinition pattern,
                                 GeneratedTypeRegistry typeRegistry,
                                 String patternName) {
        String returnType = pattern.getSemantics().getOutput()
                .map(TypeLit::getRepresentedType)
                .map(type -> type.accept(typeRegistry))
                .orElse("void");
        String argType = pattern.getSemantics().getInput()
                .getRepresentedType()
                .accept(typeRegistry);

        emitter.emitLine("%s* %s(%s* arg) {", returnType, patternName, argType);

        emitter.indent();

        emitter.unIndent();
        emitter.emitLine("}");
    }
}
