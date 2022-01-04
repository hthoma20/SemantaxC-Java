package com.semantax.phase.annotator;

import com.semantax.ast.node.AstNode;
import com.semantax.ast.node.Expression;
import com.semantax.ast.node.ParsableExpression;
import com.semantax.ast.node.list.NameTypePairList;
import com.semantax.ast.node.literal.ArrayLit;
import com.semantax.ast.node.literal.BoolLit;
import com.semantax.ast.node.literal.FunctionLit;
import com.semantax.ast.node.literal.IntLit;
import com.semantax.ast.node.literal.NameParsableExpressionPair;
import com.semantax.ast.node.literal.RecordLit;
import com.semantax.ast.node.literal.StringLit;
import com.semantax.ast.node.literal.type.ArrayTypeLit;
import com.semantax.ast.node.literal.type.BoolTypeLit;
import com.semantax.ast.node.literal.type.FuncTypeLit;
import com.semantax.ast.node.literal.type.IntTypeLit;
import com.semantax.ast.node.literal.type.NameTypeLitPair;
import com.semantax.ast.node.literal.type.RecordTypeLit;
import com.semantax.ast.node.literal.type.StringTypeLit;
import com.semantax.ast.node.literal.type.TypeLit;
import com.semantax.ast.node.progcall.DeclProgCall;
import com.semantax.ast.node.progcall.DynamicProgcall;
import com.semantax.ast.type.ArrayType;
import com.semantax.ast.type.BoolType;
import com.semantax.ast.type.FuncType;
import com.semantax.ast.type.IntType;
import com.semantax.ast.type.NameTypePair;
import com.semantax.ast.type.RecordType;
import com.semantax.ast.type.StringType;
import com.semantax.ast.type.Type;
import com.semantax.ast.type.TypeType;
import com.semantax.ast.type.VoidType;
import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.TraversalVisitor;
import com.semantax.error.ErrorType;
import com.semantax.logger.ErrorLogger;

import javax.inject.Inject;
import java.util.Set;

public class DefaultTypeAnnotator implements TypeAnnotator  {

    private final TypeAssignabilityChecker typeAssignabilityChecker;
    private final RecordTypeUtil recordTypeUtil;
    private final ErrorLogger errorLogger;

    @Inject
    public DefaultTypeAnnotator(TypeAssignabilityChecker typeAssignabilityChecker,
                                RecordTypeUtil recordTypeUtil, ErrorLogger errorLogger) {
        this.typeAssignabilityChecker = typeAssignabilityChecker;
        this.recordTypeUtil = recordTypeUtil;
        this.errorLogger = errorLogger;
    }

    public boolean annotate(AstNode node) {
        TypeAnnotatingVisitor visitor = new TypeAnnotatingVisitor();
        node.accept(visitor);
        return !visitor.hasError;
    }

    private class TypeAnnotatingVisitor extends TraversalVisitor<Void> {

        private boolean hasError = false;

        private void error(ErrorType errorType, FilePos filePos, String message, Object... args) {
            hasError = true;
            errorLogger.error(errorType, filePos, message, args);
        }

        @Override
        public Void visit(ParsableExpression parsableExpression) {
            // if we see a PE which is already parsed to an expression, then
            // it has already been type annotated
            if (!parsableExpression.hasExpression()) {
                super.visit(parsableExpression);
            }
            return null;
        }

        @Override
        public Void visit(IntLit intLit) {
            intLit.setType(IntType.INT_TYPE);
            return null;
        }

        @Override
        public Void visit(BoolLit boolLit) {
            boolLit.setType(BoolType.BOOL_TYPE);
            return null;
        }

        @Override
        public Void visit(StringLit stringLit) {
            stringLit.setType(StringType.STRING_TYPE);
            return null;
        }

        @Override
        public Void visit(ArrayLit arrayLit) {
            super.visit(arrayLit);

            if (arrayLit.getValues().isEmpty()) {
                arrayLit.setType(ArrayType.EMPTY_TYPE);
                return null;
            }

            ParsableExpression firstElement = arrayLit.getValues().get(0);
            Type subType = firstElement.getExpression().getType();

            for (ParsableExpression element : arrayLit.getValues()) {
                if (!typeAssignabilityChecker.isAssignable(subType, element.getExpression().getType())) {
                    error(ErrorType.HETEROGENEOUS_ARRAY, arrayLit.getFilePos(),
                            "Array does not have elements of the same type");
                    return null;
                }
            }

            arrayLit.setType(ArrayType.builder()
                    .subType(subType)
                    .build());
            return null;
        }

        @Override
        public Void visit(RecordLit recordLit) {
            super.visit(recordLit);

            if (recordLit.getNameParsableExpressionPairs().isEmpty()) {
                recordLit.setType(RecordType.EMPTY_TYPE);
                return null;
            }

            // check that all names are unique
            Set<String> duplicateNames = recordTypeUtil.getDuplicateNames(recordLit);
            if (!duplicateNames.isEmpty()) {
                error(ErrorType.DUPLICATE_RECORD_NAME, recordLit.getFilePos(),
                        "Record has duplicate name(s): %s", String.join(",", duplicateNames));
                return null;
            }

            NameTypePairList nameTypePairs = new NameTypePairList();
            for (NameParsableExpressionPair nameExpressionPair : recordLit.getNameParsableExpressionPairs()) {
                nameTypePairs.add(NameTypePair.builder()
                        .name(nameExpressionPair.getName())
                        .type(nameExpressionPair.getExpression().getExpression().getType())
                        .build());
            }

            recordLit.setType(RecordType.builder()
                    .nameTypePairs(nameTypePairs)
                    .build());
            return null;
        }

        @Override
        public Void visit(FunctionLit functionLit) {

            // the input types and output types may have already been annotated
            if (!functionLit.getInput().hasType()) {
                functionLit.getInput().accept(this);
            }
            functionLit.getOutput().ifPresent(output -> {
                if (!output.hasType()) {
                    output.accept(this);
                }
            });
            functionLit.getStatements().ifPresent(statements -> statements.accept(this));
            functionLit.getReturnExpression().ifPresent(expression -> expression.accept(this));

            functionLit.setType(FuncType.builder()
                    .inputType(functionLit.getInput().getRepresentedType())
                    .outputType(functionLit.getOutput().map(TypeLit::getRepresentedType))
                    .build());

            return null;
        }

        @Override
        public Void visit(IntTypeLit typeLit) {
            typeLit.setType(TypeType.TYPE_TYPE);
            typeLit.setRepresentedType(IntType.INT_TYPE);
            return null;
        }

        @Override
        public Void visit(BoolTypeLit typeLit) {
            typeLit.setType(TypeType.TYPE_TYPE);
            typeLit.setRepresentedType(BoolType.BOOL_TYPE);
            return null;
        }

        @Override
        public Void visit(StringTypeLit typeLit) {
            typeLit.setType(TypeType.TYPE_TYPE);
            typeLit.setRepresentedType(StringType.STRING_TYPE);
            return null;
        }

        @Override
        public Void visit(ArrayTypeLit arrayTypeLit) {
            super.visit(arrayTypeLit);

            arrayTypeLit.setType(TypeType.TYPE_TYPE);
            arrayTypeLit.setRepresentedType(ArrayType.builder()
                    .subType(arrayTypeLit.getSubType().getRepresentedType())
                    .build());
            return null;
        }

        @Override
        public Void visit(RecordTypeLit recordTypeLit) {

            if (recordTypeLit == RecordTypeLit.EMPTY_RECORD) {
                return null;
            }

            super.visit(recordTypeLit);

            // check that all names are unique
            Set<String> duplicateNames = recordTypeUtil.getDuplicateNames(recordTypeLit);
            if (!duplicateNames.isEmpty()) {
                error(ErrorType.DUPLICATE_RECORD_TYPE_NAME, recordTypeLit.getFilePos(),
                        "Record type has duplicate name(s): %s", String.join(",", duplicateNames));
                return null;
            }

            recordTypeLit.setType(TypeType.TYPE_TYPE);

            NameTypePairList nameTypePairs = new NameTypePairList();
            for (NameTypeLitPair nameTypeLitPair : recordTypeLit.getNameTypeLitPairs()) {
                nameTypePairs.add(NameTypePair.builder()
                        .name(nameTypeLitPair.getName())
                        .type(nameTypeLitPair.getType().getRepresentedType())
                        .build());
            }

            recordTypeLit.setRepresentedType(RecordType.builder()
                    .nameTypePairs(nameTypePairs)
                    .build());
            return null;
        }

        @Override
        public Void visit(FuncTypeLit funcTypeLit) {
            super.visit(funcTypeLit);

            funcTypeLit.setType(TypeType.TYPE_TYPE);

            funcTypeLit.setRepresentedType(FuncType.builder()
                    .inputType(funcTypeLit.getInputType().getRepresentedType())
                    .outputType(funcTypeLit.getOutputType().map(TypeLit::getRepresentedType))
                    .build());

            return null;
        }

        @Override
        public Void visit(DeclProgCall progCall) {
            super.visit(progCall);
            progCall.setType(VoidType.VOID_TYPE);
            return null;
        }

        @Override
        public Void visit(DynamicProgcall progCall) {
            super.visit(progCall);

            // TODO: fix this jankyness when generics are properly implemented
            if (progCall.getName().equals("invokefun")) {
                if(progCall.getSubExpressions().size() < 1) {
                    error(ErrorType.INVOKE_FUN_BAD_ARG, progCall.getFilePos(),
                            "@invokefun expects a function and an argument");
                    return null;
                }
                Expression first = progCall.getSubExpressions().get(0).getExpression();
                if (!(first.getType() instanceof FuncType)) {
                    error(ErrorType.INVOKE_FUN_BAD_ARG, progCall.getFilePos(),
                            "@invokefun expects a function and an argument");
                    return null;
                }
                FuncType funcType = (FuncType) first.getType();
                progCall.setType(funcType.getOutputType().orElse(VoidType.VOID_TYPE));
            }
            else if (progCall.getName().equals("arrayget")) {
                if(progCall.getSubExpressions().size() < 1) {
                    error(ErrorType.ARRAY_GET_BAD_ARG, progCall.getFilePos(),
                            "@arrayget expects an array and an index");
                    return null;
                }
                Expression first = progCall.getSubExpressions().get(0).getExpression();
                if (!(first.getType() instanceof ArrayType)) {
                    error(ErrorType.ARRAY_GET_BAD_ARG, progCall.getFilePos(),
                            "@arrayget expects an array and an index");
                    return null;
                }
                ArrayType arrayType = (ArrayType) first.getType();
                progCall.setType(arrayType.getSubType());
            }
            else {
                progCall.setType(progCall.getReturnType().orElse(VoidType.VOID_TYPE));
            }
            return null;
        }
    }
}
