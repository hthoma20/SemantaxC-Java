package com.semantax.phase.codegen;

import com.semantax.ast.node.AstNode;
import com.semantax.ast.node.Expression;
import com.semantax.ast.node.VariableReference;
import com.semantax.ast.node.literal.BoolLit;
import com.semantax.ast.node.literal.IntLit;
import com.semantax.ast.node.literal.RecordLit;
import com.semantax.ast.node.literal.StringLit;
import com.semantax.ast.node.literal.type.RecordTypeLit;
import com.semantax.ast.node.pattern.PatternInvocation;
import com.semantax.ast.node.progcall.DynamicProgcall;
import com.semantax.ast.visitor.TraversalVisitor;
import lombok.AllArgsConstructor;

import javax.inject.Inject;
import java.util.Iterator;

public class ExpressionCodeGenerator {

    @Inject
    public ExpressionCodeGenerator() { }

    public void generateExpression(CodeEmitter emitter,
                                   GeneratedTypeRegistry typeRegistry,
                                   GeneratedPatternRegistry patternRegistry,
                                   Expression expression) {
        expression.accept(new ExpressionTraversalVisitor(emitter, typeRegistry, patternRegistry));
    }

    @AllArgsConstructor
    private static class ExpressionTraversalVisitor extends TraversalVisitor<Void> {

        private final CodeEmitter emitter;
        private final GeneratedTypeRegistry typeRegistry;
        private final GeneratedPatternRegistry patternRegistry;

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

            commaSeparatedVisit(recordLit.getNameParsableExpressionPairs().iterator());

            emitter.emit(")");
            return null;
        }

        @Override
        public Void visit(DynamicProgcall dynamicProgcall) {
            emitter.emit("%s(", dynamicProgcall.getName());
            commaSeparatedVisit(dynamicProgcall.getSubExpressions().iterator());
            emitter.emit(")");

            return null;
        }

        private void emitPatternInvocationArg(PatternInvocation patternInvocation) {
            RecordTypeLit typeLit = patternInvocation.getPatternDefinition()
                    .getSemantics()
                    .getInput();
            String recordName = typeLit.getRepresentedType().accept(typeRegistry);
            emitter.emit("new_%s(", recordName);

            Iterator<Expression> iterator = typeLit.getNameTypeLitPairs()
                    .stream()
                    .map(nameTypeLitPair -> patternInvocation.getArguments().get(nameTypeLitPair.getName()))
                    .iterator();

            commaSeparatedVisit(iterator);

            emitter.emit(")");
        }

        @Override
        public Void visit(PatternInvocation patternInvocation) {

            emitter.emit("%s(", patternRegistry.getPatternName(patternInvocation.getPatternDefinition()));
            emitPatternInvocationArg(patternInvocation);
            emitter.emit(")");

            return null;
        }


        @Override
        public Void visit(VariableReference variableReference) {
            emitter.emit("nullptr");
            return null;
        }

        /**
         * Generate expressions by visiting each Ast node in the given iterator
         * Between each visitation, emit a comma
         * @param iterator the AstNodes to visit
         */
        private void commaSeparatedVisit(Iterator<? extends AstNode> iterator) {
            iterator.next().accept(this);
            iterator.forEachRemaining(node -> {
                emitter.emit(", ");
                node.accept(this);
            });
        }
    }
}
