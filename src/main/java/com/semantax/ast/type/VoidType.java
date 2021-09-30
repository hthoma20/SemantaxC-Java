package com.semantax.ast.type;

import com.semantax.ast.visitor.AstVisitor;

/**
 * Type used to mark:
 * - the return type of a non-returning function
 * - the subtype of an empty array
 */
public class VoidType extends Type {
    public static final VoidType VOID_TYPE = new VoidType();

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
