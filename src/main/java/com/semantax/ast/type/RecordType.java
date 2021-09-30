package com.semantax.ast.type;

import com.semantax.ast.node.list.NameTypeLitPairList;
import com.semantax.ast.visitor.AstVisitor;
import lombok.Builder;

@Builder
public class RecordType extends Type {
//    private final NameTypeLitPairList nameTypePairs;

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
