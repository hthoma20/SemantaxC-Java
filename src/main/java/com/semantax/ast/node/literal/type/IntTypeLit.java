package com.semantax.ast.node.literal.type;

import com.semantax.ast.visitor.ASTVisitor;

public class IntTypeLit extends TypeLit {
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
