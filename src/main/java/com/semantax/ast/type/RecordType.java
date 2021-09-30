package com.semantax.ast.type;

import com.semantax.ast.node.list.NameTypePairList;
import com.semantax.ast.visitor.AstVisitor;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "Builder")
public class RecordType extends Type {
    @Getter
    private final NameTypePairList nameTypePairs;

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
