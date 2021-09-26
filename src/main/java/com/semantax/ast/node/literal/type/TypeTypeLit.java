package com.semantax.ast.node.literal.type;

import com.semantax.ast.visitor.AstVisitor;

public class TypeTypeLit extends TypeLit {
    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
