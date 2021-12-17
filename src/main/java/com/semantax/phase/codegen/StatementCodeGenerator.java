package com.semantax.phase.codegen;


import com.semantax.ast.node.Expression;
import com.semantax.ast.node.Statement;
import com.semantax.ast.node.VariableDeclaration;
import com.semantax.ast.node.list.StatementList;
import com.semantax.ast.node.literal.type.NameTypeLitPair;
import com.semantax.ast.node.progcall.BindProgCall;
import com.semantax.ast.node.progcall.DeclProgCall;
import com.semantax.ast.node.progcall.StaticProgCall;
import com.semantax.ast.type.VoidType;
import com.semantax.ast.visitor.BaseAstVisitor;
import com.semantax.exception.CompilerException;
import lombok.AllArgsConstructor;

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
        StaticProgCallGenerator staticProgCallGenerator = new StaticProgCallGenerator(emitter, nameRegistry);

        for (Statement statement : statements) {
            if (statement.getExpression() instanceof StaticProgCall) {
                statement.getExpression().accept(staticProgCallGenerator);
            }
            else {
                expressionCodeGenerator.generateExpression(emitter, nameRegistry, statement.getExpression());
                if (statement.getExpression().getType() != VoidType.VOID_TYPE) {
                    emitter.emitLine("popRoot();");
                }
            }
        }
    }

    @AllArgsConstructor
    private class StaticProgCallGenerator extends BaseAstVisitor<Void> {
        private final CodeEmitter emitter;
        private final GeneratedNameRegistry nameRegistry;

        @Override
        public Void visit(DeclProgCall declProgCall) {
            return null;
        }

        @Override
        public Void visit(BindProgCall bindProgCall) {

            VariableDeclaration declaration = bindProgCall.getVariableReference().getDeclaration();
            Expression expression = bindProgCall.getValue();

            expressionCodeGenerator.generateExpression(emitter, nameRegistry, expression);

            if (declaration instanceof DeclProgCall) {
                emitter.emitLine("%s->val = popRoot();", nameRegistry.getVariableName((DeclProgCall) declaration));
            }
            else if (declaration instanceof NameTypeLitPair) {
                // this is a reference to the argument of a function or pattern
                emitter.emitLine("arg->%s = popRoot();", declaration.getDeclName());
            }
            else {
                throw CompilerException.of("Unexpected declaration type");
            }

            return null;
        }
    }
}
