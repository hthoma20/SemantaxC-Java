package com.semantax.phase.codegen;


import com.semantax.ast.node.Statement;
import com.semantax.ast.node.list.StatementList;
import com.semantax.ast.node.progcall.DeclProgCall;

import javax.inject.Inject;

public class StatementCodeGenerator {

    private final ExpressionCodeGenerator expressionCodeGenerator;

    @Inject
    public StatementCodeGenerator(ExpressionCodeGenerator expressionCodeGenerator) {
        this.expressionCodeGenerator = expressionCodeGenerator;
    }

    public void generateStatements(CodeEmitter emitter,
                                   GeneratedTypeRegistry typeRegistry,
                                   GeneratedPatternRegistry patternRegistry,
                                   StatementList statements) {
        for (Statement statement : statements) {
            if (statement.getExpression() instanceof DeclProgCall) {
                continue;
            }
            emitter.beginLine();
            expressionCodeGenerator.generateExpression(emitter, typeRegistry, patternRegistry, statement.getExpression());
            emitter.endLine(";");
        }
    }
}
