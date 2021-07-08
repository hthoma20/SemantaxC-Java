package com.semantax.ast.node.literal.type;

import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.ASTVisitor;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "Builder")
@Getter
public class FuncTypeLit extends TypeLit {

    @lombok.Builder.Default
    private TypeLit inputType = RecordTypeLit.EMPTY_RECORD;
    @lombok.Builder.Default
    private TypeLit outputType = VoidTypeLit.VOID_TYPE_LIT;

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public static class Builder {

        public FuncTypeLit buildWith(FilePos filePos) {
            FuncTypeLit funcTypeLit = build();
            funcTypeLit.setFilePos(filePos);
            return funcTypeLit;
        }


    }
}
