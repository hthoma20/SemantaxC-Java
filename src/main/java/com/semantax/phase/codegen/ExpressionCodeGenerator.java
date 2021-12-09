package com.semantax.phase.codegen;

import com.semantax.ast.node.Expression;
import com.semantax.ast.node.VariableReference;
import com.semantax.ast.node.literal.BoolLit;
import com.semantax.ast.node.literal.IntLit;
import com.semantax.ast.node.literal.NameParsableExpressionPair;
import com.semantax.ast.node.literal.RecordLit;
import com.semantax.ast.node.literal.StringLit;
import com.semantax.ast.visitor.TraversalVisitor;
import lombok.AllArgsConstructor;

import javax.inject.Inject;
import java.util.Iterator;

public class ExpressionCodeGenerator {

    @Inject
    public ExpressionCodeGenerator() { }

    public void generateExpression(CodeEmitter emitter, GeneratedTypeRegistry typeRegistry, Expression expression) {
        expression.accept(new ExpressionTraversalVisitor(emitter, typeRegistry));
    }

    @AllArgsConstructor
    private static class ExpressionTraversalVisitor extends TraversalVisitor<Void> {

        private final CodeEmitter emitter;
        private final GeneratedTypeRegistry typeRegistry;

        @Override
        public Void visit(BoolLit boolLit) {
            emitter.emit("new_Bool(%b)", boolLit.getValue());
            return null;
        }

        @Override
        public Void visit(IntLit intLit) {
            emitter.emit("new_Int(%d)", intLit.getValue());
            return null;
        }

        @Override
        public Void visit(StringLit stringLit) {
            emitter.emit("new_String(\"%s\")", stringLit.getValue());
            return null;
        }

        @Override
        public Void visit(RecordLit recordLit) {

            if (recordLit.getNameParsableExpressionPairs().size() == 0) {
                emitter.emit("nullptr");
                return null;
            }

            String recordName = recordLit.getType().accept(typeRegistry);
            emitter.emit("new_%s(", recordName);

            Iterator<NameParsableExpressionPair> iterator = recordLit.getNameParsableExpressionPairs().iterator();
            iterator.next().getExpression().getExpression().accept(this);
            iterator.forEachRemaining(nameParsableExpressionPair -> {
                emitter.emit(", ");
                nameParsableExpressionPair.accept(this);
            });

            emitter.emit(")");
            return null;
        }

        @Override
        public Void visit(VariableReference variableReference) {
            emitter.emit("nullptr");
            return null;
        }


    }
}
