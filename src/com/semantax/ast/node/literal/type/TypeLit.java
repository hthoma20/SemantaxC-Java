package com.semantax.ast.node.literal.type;

import com.semantax.ast.node.literal.Literal;
import com.semantax.ast.visitor.ASTVisitor;

public abstract class TypeLit extends Literal {

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
