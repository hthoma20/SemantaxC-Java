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
                                 GeneratedNameRegistry nameRegistry,
                                 Program program) {
        for (Module module : program.getModules()) {
            for (PatternDefinition pattern : module.getPatterns()) {
                generatePattern(emitter, nameRegistry, pattern);
                emitter.emitLine("");
            }
        }
    }

    private void generatePattern(CodeEmitter emitter,
                                 GeneratedNameRegistry nameRegistry,
                                 PatternDefinition pattern) {

        String argType = nameRegistry.getTypeName(pattern.getSemantics().getInput()
                .getRepresentedType());

        emitter.emitLine("void %s() {", nameRegistry.getPatternName(pattern));
        emitter.indent();
        emitter.emitLine("%s* arg = (%s*) popRoot();", argType, argType);

        generatePatternBody(emitter, nameRegistry, pattern);

        emitter.unIndent();
        emitter.emitLine("}");
    }

    private void generatePatternBody(CodeEmitter emitter,
                                     GeneratedNameRegistry nameRegistry,
                                     PatternDefinition pattern) {
        FunctionLit function = pattern.getSemantics();

        if (function.getReturnExpression().isPresent()) {
            Expression returnExpression = function.getReturnExpression().get().getExpression();
            expressionCodeGenerator.generateExpression(emitter, nameRegistry, returnExpression);
        }
        else {
            StatementList statements = function.getStatements().get();
            statementCodeGenerator.generateStatements(emitter, nameRegistry, statements);
        }
    }
}
