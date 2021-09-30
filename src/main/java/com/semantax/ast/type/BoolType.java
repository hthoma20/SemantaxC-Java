package com.semantax.ast.type;

import com.semantax.ast.visitor.AstVisitor;

public class BoolType extends Type {
    public static final BoolType BOOL_TYPE = new BoolType();

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
