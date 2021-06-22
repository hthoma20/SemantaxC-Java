package com.semantax.ast.node.literal;

import com.semantax.ast.node.list.NameExpressionPairList;
import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.ASTVisitor;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "Builder")
public class RecordLit extends Literal {
    @Getter
    private NameExpressionPairList nameExpressionPairs;

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public static class Builder {

        public RecordLit buildWith(FilePos filePos) {
            RecordLit recordLit = build();
            recordLit.setFilePos(filePos);
            return recordLit;
        }

    }
}
