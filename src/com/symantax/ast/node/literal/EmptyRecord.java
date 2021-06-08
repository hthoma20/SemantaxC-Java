package com.symantax.ast.node.literal;

import com.symantax.ast.visitor.ASTVisitor;

/**
 * The Empty Record is literally "()"
 * It can represent the empty record literal, or empty tuple literal based on context
 */
public class EmptyRecord extends Literal {

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        EmptyRecord self = this;
        return visitor.visit(self);
    }

}
