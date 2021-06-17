package com.semantax.ast.node;

import com.semantax.ast.visitor.ASTVisitor;
import lombok.Builder;
import lombok.Getter;

@Builder
public class Statement extends AstNode {
    @Getter
    private Expression expression;

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
