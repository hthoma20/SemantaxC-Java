package com.semantax.ast.node.literal;

import com.semantax.ast.node.list.NameParsableExpressionPairList;
import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.AstVisitor;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "Builder")
public class RecordLit extends Literal {
    @Getter
    private NameParsableExpressionPairList nameParsableExpressionPairs;

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
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
