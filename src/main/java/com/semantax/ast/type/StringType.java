package com.semantax.ast.type;

import com.semantax.ast.visitor.AstVisitor;

public class StringType extends Type {
    public static final StringType STRING_TYPE = new StringType();

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
