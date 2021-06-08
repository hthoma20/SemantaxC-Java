package com.symantax.ast.node.literal.type;

import com.symantax.ast.node.Expression;
import com.symantax.ast.node.literal.Literal;
import com.symantax.ast.util.FilePos;
import com.symantax.ast.visitor.ASTVisitor;

public abstract class TypeLit extends Literal {

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
