package com.semantax.phase.annotator;

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
import com.semantax.ast.node.progcall.AddIntProgCall;
import com.semantax.ast.node.progcall.DeclProgCall;
import com.semantax.ast.node.progcall.PrintIntProgCall;
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
import com.semantax.ast.visitor.TraversalVisitor;
import com.semantax.error.ErrorType;
import com.semantax.logger.ErrorLogger;

import javax.inject.Inject;
import java.util.Set;

public class DefaultTypeAnnotator extends TraversalVisitor<Boolean> implements TypeAnnotator  {

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

    @Override
    public Boolean visit(ParsableExpression parsableExpression) {
        // if we see a PE which is already parsed to an expression, then
        // it has already been type annotated
        if (!parsableExpression.hasExpression()) {
            super.visit(parsableExpression);
        }
        return true;
    }

    @Override
    public Boolean visit(IntLit intLit) {
        intLit.setType(IntType.INT_TYPE);
        return true;
    }

    @Override
    public Boolean visit(BoolLit boolLit) {
        boolLit.setType(BoolType.BOOL_TYPE);
        return true;
    }

    @Override
    public Boolean visit(StringLit stringLit) {
        stringLit.setType(StringType.STRING_TYPE);
        return true;
    }

    @Override
    public Boolean visit(ArrayLit arrayLit) {
        super.visit(arrayLit);

        if (arrayLit.getValues().isEmpty()) {
            arrayLit.setType(ArrayType.EMPTY_TYPE);
            return true;
        }

        ParsableExpression firstElement = arrayLit.getValues().get(0);
        Type subType = firstElement.getExpression().getType();

        for (ParsableExpression element : arrayLit.getValues()) {
            if (!typeAssignabilityChecker.isAssignable(subType, element.getExpression().getType())) {
                errorLogger.error(ErrorType.HETEROGENEOUS_ARRAY, arrayLit.getFilePos(),
                        "Array does not have elements of the same type");
                return false;
            }
        }

        arrayLit.setType(ArrayType.builder()
                .subType(subType)
                .build());
        return true;
    }

    @Override
    public Boolean visit(RecordLit recordLit) {
        super.visit(recordLit);

        if (recordLit.getNameParsableExpressionPairs().isEmpty()) {
            recordLit.setType(RecordType.EMPTY_TYPE);
            return true;
        }

        // check that all names are unique
        Set<String> duplicateNames = recordTypeUtil.getDuplicateNames(recordLit);
        if (!duplicateNames.isEmpty()) {
            errorLogger.error(ErrorType.DUPLICATE_RECORD_NAME, recordLit.getFilePos(),
                    "Record has duplicate name(s): %s",
                    String.join(",", duplicateNames));
            return false;
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
        return true;
    }

    @Override
    public Boolean visit(FunctionLit functionLit) {
        super.visit(functionLit);

        functionLit.setType(FuncType.builder()
                .inputType(functionLit.getInput().getRepresentedType())
                .outputType(functionLit.getOutput().map(TypeLit::getRepresentedType))
                .build());

        return true;
    }

    @Override
    public Boolean visit(IntTypeLit typeLit) {
        typeLit.setType(TypeType.TYPE_TYPE);
        typeLit.setRepresentedType(IntType.INT_TYPE);
        return true;
    }

    @Override
    public Boolean visit(BoolTypeLit typeLit) {
        typeLit.setType(TypeType.TYPE_TYPE);
        typeLit.setRepresentedType(BoolType.BOOL_TYPE);
        return true;
    }

    @Override
    public Boolean visit(StringTypeLit typeLit) {
        typeLit.setType(TypeType.TYPE_TYPE);
        typeLit.setRepresentedType(StringType.STRING_TYPE);
        return true;
    }

    @Override
    public Boolean visit(ArrayTypeLit arrayTypeLit) {
        super.visit(arrayTypeLit);

        arrayTypeLit.setType(TypeType.TYPE_TYPE);
        arrayTypeLit.setRepresentedType(ArrayType.builder()
            .subType(arrayTypeLit.getSubType().getRepresentedType())
            .build());
        return true;
    }

    @Override
    public Boolean visit(RecordTypeLit recordTypeLit) {
        super.visit(recordTypeLit);

        // check that all names are unique
        Set<String> duplicateNames = recordTypeUtil.getDuplicateNames(recordTypeLit);
        if (!duplicateNames.isEmpty()) {
            errorLogger.error(ErrorType.DUPLICATE_RECORD_TYPE_NAME, recordTypeLit.getFilePos(),
                    "Record type has duplicate name(s): %s",
                    String.join(",", duplicateNames));
            return false;
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
        return true;
    }

    @Override
    public Boolean visit(FuncTypeLit funcTypeLit) {
        super.visit(funcTypeLit);

        funcTypeLit.setType(TypeType.TYPE_TYPE);

        funcTypeLit.setRepresentedType(FuncType.builder()
                .inputType(funcTypeLit.getInputType().getRepresentedType())
                .outputType(funcTypeLit.getOutputType().map(TypeLit::getRepresentedType))
                .build());

        return true;
    }

    @Override
    public Boolean visit(DeclProgCall progCall) {
        super.visit(progCall);
        progCall.setType(VoidType.VOID_TYPE);
        return true;
    }

    @Override
    public Boolean visit(PrintIntProgCall progCall) {
        super.visit(progCall);
        progCall.setType(VoidType.VOID_TYPE);
        return true;
    }

    @Override
    public Boolean visit(AddIntProgCall progCall) {
        super.visit(progCall);
        progCall.setType(IntType.INT_TYPE);
        return true;
    }

}
