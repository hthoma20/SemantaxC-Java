package com.semantax.phase.annotator;

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
import com.semantax.ast.visitor.BaseAstVisitor;
import com.semantax.exception.CompilerException;
import lombok.AllArgsConstructor;

public class DefaultTypeAssignabilityChecker implements TypeAssignabilityChecker {
    @Override
    public boolean isAssignable(Type lhs, Type rhs) {
        return lhs.accept(new AssignabilityVisitor(rhs));
    }

    /**
     * This class is used to check whether a rhs Type is assignable to a lhs type.
     * The rhs is provided in the constructor. The visit methods accept the lhs and
     * return whether the instance's rhs is assignable to the given lhs.
     * Example use:
     * <code>
     *     Type lhs, rhs;
     *     boolean canAssign = lhs.accept(new AssignabilityVisitor(rhs));
     * </code>
     *
     */
    @AllArgsConstructor
    private class AssignabilityVisitor extends BaseAstVisitor<Boolean> {
        private final Type rhs;

        @Override
        public Boolean visit(TypeType lhs) {
            return rhs instanceof TypeType ||
                    rhs instanceof RecordType ||
                    rhs instanceof ArrayType;
        }

        @Override
        public Boolean visit(IntType lhs) {
            return rhs instanceof IntType;
        }

        @Override
        public Boolean visit(BoolType lhs) {
            return rhs instanceof BoolType;
        }

        @Override
        public Boolean visit(StringType lhs) {
            return rhs instanceof StringType;
        }

        @Override
        public Boolean visit(VoidType lhs) {
            return rhs instanceof VoidType;
        }

        @Override
        public Boolean visit(ArrayType lhs) {

            if (lhs.getSubType() instanceof VoidType) {
                throw CompilerException.of("lhs type should not be an void/empty array");
            }

            if (!(rhs instanceof ArrayType)) {
                return false;
            }

            ArrayType rhsArrayType = (ArrayType) rhs;

            if (rhsArrayType.getSubType() instanceof VoidType) {
                return true;
            }

            return isAssignable(lhs.getSubType(), rhsArrayType.getSubType());
        }

        @Override
        public Boolean visit(RecordType lhs) {
            if (!(rhs instanceof RecordType)) {
                return false;
            }

            RecordType rhsRecordType = (RecordType) rhs;

            if (lhs.getNameTypePairs().size() != rhsRecordType.getNameTypePairs().size()) {
                return false;
            }

            for (int i = 0; i < lhs.getNameTypePairs().size(); i++) {
                NameTypePair lhsNameTypePair = lhs.getNameTypePairs().get(i);
                NameTypePair rhsNameTypePair = rhsRecordType.getNameTypePairs().get(i);

                if (!lhsNameTypePair.getName().equals(rhsNameTypePair.getName())) {
                    return false;
                }
                if (!isAssignable(lhsNameTypePair.getType(), rhsNameTypePair.getType())) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public Boolean visit(FuncType lhs) {
            if (!(rhs instanceof FuncType)) {
                return false;
            }

            FuncType rhsFuncType = (FuncType) rhs;

            // the function must be able to accept the left hand type
            if (!isAssignable(rhsFuncType.getInputType(), lhs.getInputType())) {
                return false;
            }

            // if one has an output type and one doesn't, they are not assignable
            if (lhs.getOutputType().isPresent() != rhsFuncType.getOutputType().isPresent()) {
                return false;
            }

            // if they are both non-returning, they are assignable
            if (!lhs.getOutputType().isPresent()) {
                return true;
            }

            // at this point we know both are returning
            return isAssignable(lhs.getOutputType().get(), ((FuncType) rhs).getOutputType().get());
        }
    }
}
