package com.semantax.ast.node.literal;

import com.semantax.ast.visitor.ASTVisitor;
import com.semantax.ast.node.Expression;

public abstract class Literal extends Expression {

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
