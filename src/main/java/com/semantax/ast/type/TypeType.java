package com.semantax.ast.type;

import com.semantax.ast.visitor.AstVisitor;

public class TypeType extends Type {

    public static final TypeType TYPE_TYPE = new TypeType();

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
