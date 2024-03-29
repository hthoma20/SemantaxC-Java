package com.semantax.ast.type;

import com.semantax.ast.visitor.AstVisitor;

public class IntType extends Type {
    public static final IntType INT_TYPE = new IntType();

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
