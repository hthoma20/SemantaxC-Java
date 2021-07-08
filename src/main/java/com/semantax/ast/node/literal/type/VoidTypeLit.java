package com.semantax.ast.node.literal.type;

import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.ASTVisitor;

/**
 * Represents the "type" of a function that returns no value
 */
public final class VoidTypeLit extends TypeLit {

    public static final VoidTypeLit VOID_TYPE_LIT = new VoidTypeLit();

    private VoidTypeLit() {
        this.setFilePos(new FilePos(-1, -1));
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
