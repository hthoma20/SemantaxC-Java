package com.semantax.phase.codegen;

import com.semantax.ast.node.Expression;
import com.semantax.ast.node.Module;
import com.semantax.ast.node.Program;
import com.semantax.ast.node.list.StatementList;
import com.semantax.ast.node.literal.FunctionLit;
import com.semantax.ast.node.literal.type.TypeLit;
import com.semantax.ast.node.pattern.PatternDefinition;

import javax.inject.Inject;

/**
 * Generate code for the main function
 */
public class PatternCodeGenerator {

    private final ExpressionCodeGenerator expressionCodeGenerator;
    private final StatementCodeGenerator statementCodeGenerator;

    @Inject
    public PatternCodeGenerator(ExpressionCodeGenerator expressionCodeGenerator,
                                StatementCodeGenerator statementCodeGenerator) {
        this.expressionCodeGenerator = expressionCodeGenerator;
        this.statementCodeGenerator = statementCodeGenerator;
    }

    public void generatePatterns(CodeEmitter emitter,
                                 GeneratedPatternRegistry patternRegistry,
                                 GeneratedTypeRegistry typeRegistry,
                                 Program program) {
        for (Module module : program.getModules()) {
            for (PatternDefinition pattern : module.getPatterns()) {
                generatePattern(emitter, typeRegistry, patternRegistry, pattern);
                emitter.emitLine("");
            }
        }
    }

    private void generatePattern(CodeEmitter emitter,
                                 GeneratedTypeRegistry typeRegistry,
                                 GeneratedPatternRegistry patternRegistry,
                                 PatternDefinition pattern) {

        String argType = pattern.getSemantics().getInput()
                .getRepresentedType()
                .accept(typeRegistry);

        emitter.emitLine("void %s() {", patternRegistry.getPatternName(pattern));
        emitter.indent();
        emitter.emitLine("%s* arg = (%s*) popRoot();", argType, argType);

        generatePatternBody(emitter, typeRegistry, patternRegistry, pattern);

        emitter.unIndent();
        emitter.emitLine("}");
    }

    private void generatePatternBody(CodeEmitter emitter,
                                     GeneratedTypeRegistry typeRegistry,
                                     GeneratedPatternRegistry patternRegistry,
                                     PatternDefinition pattern) {
        FunctionLit function = pattern.getSemantics();

        if (function.getReturnExpression().isPresent()) {
            Expression returnExpression = function.getReturnExpression().get().getExpression();
            expressionCodeGenerator.generateExpression(emitter, typeRegistry, patternRegistry, returnExpression);
        }
        else {
            StatementList statements = function.getStatements().get();
            statementCodeGenerator.generateStatements(emitter, typeRegistry, patternRegistry, statements);
        }
    }
}
