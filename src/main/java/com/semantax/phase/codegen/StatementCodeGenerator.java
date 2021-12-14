package com.semantax.phase.codegen;


import com.semantax.ast.node.Statement;
import com.semantax.ast.node.list.StatementList;
import com.semantax.ast.node.progcall.DeclProgCall;
import com.semantax.ast.type.VoidType;

import javax.inject.Inject;

public class StatementCodeGenerator {

    private final ExpressionCodeGenerator expressionCodeGenerator;

    @Inject
    public StatementCodeGenerator(ExpressionCodeGenerator expressionCodeGenerator) {
        this.expressionCodeGenerator = expressionCodeGenerator;
    }

    public void generateStatements(CodeEmitter emitter,
                                   GeneratedNameRegistry nameRegistry,
                                   StatementList statements) {
        for (Statement statement : statements) {
            if (statement.getExpression() instanceof DeclProgCall) {
                continue;
            }
            expressionCodeGenerator.generateExpression(emitter, nameRegistry, statement.getExpression());
            if (statement.getExpression().getType() != VoidType.VOID_TYPE) {
                emitter.emitLine("popRoot();");
            }
        }
    }
}
