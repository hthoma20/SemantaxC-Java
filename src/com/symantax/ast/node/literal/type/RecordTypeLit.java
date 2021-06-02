package com.symantax.ast.node.literal.type;

import com.symantax.ast.visitor.ASTVisitor;

public class RecordTypeLit extends TypeLit {
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
