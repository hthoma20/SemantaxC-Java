package com.symantax.ast.node.literal;

import com.symantax.ast.node.AstNode;
import com.symantax.ast.node.Expression;
import com.symantax.ast.visitor.ASTVisitor;

public abstract class Literal extends Expression {

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
