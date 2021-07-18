package com.semantax.ast.node.literal.type;

import com.semantax.ast.visitor.ASTVisitor;

public class BoolTypeLit extends TypeLit {
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}