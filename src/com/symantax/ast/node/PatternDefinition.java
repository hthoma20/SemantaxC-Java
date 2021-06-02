package com.symantax.ast.node;

import com.symantax.ast.visitor.ASTVisitor;

public class PatternDefinition extends AstNode {

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
