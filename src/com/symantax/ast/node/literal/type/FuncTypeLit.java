package com.symantax.ast.node.literal.type;

import com.symantax.ast.node.list.NameTypePairList;
import com.symantax.ast.util.FilePos;
import com.symantax.ast.visitor.ASTVisitor;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "Builder")
@Getter
public class FuncTypeLit extends TypeLit {

    private static TypeLit EMPTY_RECORD = RecordTypeLit.builder()
            .nameTypePairs(new NameTypePairList())
            .buildWith(new FilePos(-1, -1));

    @lombok.Builder.Default
    private TypeLit inputType = EMPTY_RECORD;
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
