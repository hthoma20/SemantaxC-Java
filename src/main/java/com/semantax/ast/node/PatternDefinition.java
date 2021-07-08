package com.semantax.ast.node;

import com.semantax.ast.visitor.ASTVisitor;

public class PatternDefinition extends AstNode {

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
