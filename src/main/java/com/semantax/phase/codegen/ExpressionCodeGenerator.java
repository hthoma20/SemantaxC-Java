package com.semantax.phase.codegen;

import com.semantax.ast.node.AstNode;
import com.semantax.ast.node.Expression;
import com.semantax.ast.node.ParsableExpression;
import com.semantax.ast.node.VariableReference;
import com.semantax.ast.node.literal.BoolLit;
import com.semantax.ast.node.literal.IntLit;
import com.semantax.ast.node.literal.NameParsableExpressionPair;
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
            emitter.emitLine("new_Bool(%b);", boolLit.getValue());
            return null;
        }

        @Override
        public Void visit(IntLit intLit) {
            emitter.emitLine("new_Int(%d);", intLit.getValue());
            return null;
        }

        @Override
        public Void visit(StringLit stringLit) {
            emitter.emitLine("new_String(\"%s\");", stringLit.getValue());
            return null;
        }

        @Override
        public Void visit(RecordLit recordLit) {

            if (recordLit.getNameParsableExpressionPairs().size() == 0) {
                emitter.emitLine("pushRoot(nullptr);");
                return null;
            }

            annotateIndentedVisit(recordLit.getNameParsableExpressionPairs().stream()
                    .map(NameParsableExpressionPair::getExpression)
                    .map(ParsableExpression::getExpression)
                    .iterator());

            String recordName = recordLit.getType().accept(typeRegistry);
            emitter.emitLine("new_%s();", recordName);

            return null;
        }

        @Override
        public Void visit(DynamicProgcall dynamicProgcall) {

            annotateIndentedVisit(dynamicProgcall.getSubExpressions().iterator());
            emitter.emitLine("%s();", dynamicProgcall.getName());

            return null;
        }

        @Override
        public Void visit(PatternInvocation patternInvocation) {

            RecordTypeLit inputType = patternInvocation.getPatternDefinition()
                    .getSemantics()
                    .getInput();

            Iterator<Expression> argumentIterator = inputType.getNameTypeLitPairs()
                    .stream()
                    .map(nameTypeLitPair -> patternInvocation.getArguments().get(nameTypeLitPair.getName()))
                    .iterator();
            annotateIndentedVisit(argumentIterator);

            emitter.emitLine("new_%s();", inputType.getRepresentedType().accept(typeRegistry));

            emitter.emitLine("%s();", patternRegistry.getPatternName(patternInvocation.getPatternDefinition()));

            return null;
        }


        @Override
        public Void visit(VariableReference variableReference) {
            emitter.emitLine("pushRoot(nullptr);");
            return null;
        }

        /**
         * Generate expressions by visiting each Ast node in the given iterator
         * Indent and surround with {} for semantic clarity
         * @param iterator the AstNodes to visit
         */
        private void annotateIndentedVisit(Iterator<? extends AstNode> iterator) {

            emitter.annotateLine("{");
            emitter.annotateIndent();

            iterator.forEachRemaining(node -> {
                node.accept(this);
            });

            emitter.annotateUnIndent();
            emitter.annotateLine("}");

        }
    }
}
