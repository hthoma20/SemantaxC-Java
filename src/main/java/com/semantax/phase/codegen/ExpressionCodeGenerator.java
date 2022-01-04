package com.semantax.phase.codegen;

import com.semantax.ast.node.AstNode;
import com.semantax.ast.node.Expression;
import com.semantax.ast.node.ParsableExpression;
import com.semantax.ast.node.VariableDeclaration;
import com.semantax.ast.node.VariableReference;
import com.semantax.ast.node.literal.ArrayLit;
import com.semantax.ast.node.literal.BoolLit;
import com.semantax.ast.node.literal.FunctionLit;
import com.semantax.ast.node.literal.IntLit;
import com.semantax.ast.node.literal.NameParsableExpressionPair;
import com.semantax.ast.node.literal.RecordLit;
import com.semantax.ast.node.literal.StringLit;
import com.semantax.ast.node.literal.type.NameTypeLitPair;
import com.semantax.ast.node.literal.type.RecordTypeLit;
import com.semantax.ast.node.pattern.PatternInvocation;
import com.semantax.ast.node.progcall.DeclProgCall;
import com.semantax.ast.node.progcall.DynamicProgcall;
import com.semantax.ast.visitor.TraversalVisitor;
import com.semantax.exception.CompilerException;
import lombok.AllArgsConstructor;

import javax.inject.Inject;
import java.util.Iterator;

public class ExpressionCodeGenerator {

    @Inject
    public ExpressionCodeGenerator() { }

    public void generateExpression(CodeEmitter emitter,
                                   GeneratedNameRegistry nameRegistry,
                                   Expression expression) {
        expression.accept(new ExpressionTraversalVisitor(emitter, nameRegistry));
    }

    @AllArgsConstructor
    private static class ExpressionTraversalVisitor extends TraversalVisitor<Void> {

        private final CodeEmitter emitter;
        private final GeneratedNameRegistry nameRegistry;

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
        public Void visit(ArrayLit arrayLit) {
            annotateIndentedVisit(arrayLit.getValues().iterator());
            emitter.emitLine("new_Array(%d);", arrayLit.getValues().size());
            return null;
        }

        @Override
        public Void visit(RecordLit recordLit) {

            annotateIndentedVisit(recordLit.getNameParsableExpressionPairs().stream()
                    .map(NameParsableExpressionPair::getExpression)
                    .map(ParsableExpression::getExpression)
                    .iterator());

            String recordName = nameRegistry.getTypeName(recordLit.getType());
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

            emitter.annotateLine("// closure for pattern");
            emitter.emitLine("pushRoot(nullptr);");

            RecordTypeLit inputType = patternInvocation.getPatternDefinition()
                    .getSemantics()
                    .getInput();

            Iterator<Expression> argumentIterator = inputType.getNameTypeLitPairs()
                    .stream()
                    .map(nameTypeLitPair -> patternInvocation.getArguments().get(nameTypeLitPair.getName()))
                    .iterator();
            annotateIndentedVisit(argumentIterator);

            emitter.emitLine("new_%s();", nameRegistry.getTypeName(inputType.getRepresentedType()));

            emitter.emitLine("%s();", nameRegistry.getFunctionName(
                    patternInvocation.getPatternDefinition().getSemantics()));

            return null;
        }


        @Override
        public Void visit(VariableReference variableReference) {

            VariableDeclaration declaration = variableReference.getDeclaration();

            switch (variableReference.getScope()) {
                case LOCAL:
                case GLOBAL:
                    emitter.emitLine("pushRoot(%s->val);", nameRegistry.getVariableName(declaration));
                    break;
                case CLOSURE:
                    emitter.emitLine("pushRoot(closure->%s->val);", declaration.getDeclName());
                    break;
                default:
                    throw CompilerException.of("Unexpected variable reference scope");
            }

            return null;
        }

        @Override
        public Void visit(FunctionLit function) {

            emitter.annotateLine("{");
            emitter.annotateIndent();

            for (VariableReference enclosedVariable : function.getEnclosedVariables()) {
                VariableDeclaration declaration = enclosedVariable.getDeclaration();
                switch (enclosedVariable.getScope()) {
                    case LOCAL:
                    case GLOBAL:
                        emitter.emitLine("pushRoot(%s);", nameRegistry.getVariableName(declaration));
                        break;
                    case CLOSURE:
                        emitter.emitLine("pushRoot(closure->%s);", declaration.getDeclName());
                        break;
                    default:
                        throw CompilerException.of("Unexpected variable reference scope");
                }
            }

            emitter.annotateUnIndent();
            emitter.annotateLine("}");

            emitter.emitLine("new_%s();", nameRegistry.getFunctionName(function));
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
