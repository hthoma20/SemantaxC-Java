package com.symantax.ast.node.literal;

import com.symantax.ast.node.Expression;
import com.symantax.ast.visitor.ASTVisitor;
import lombok.Getter;
import lombok.ToString;

@ToString
public class BoolLit extends Literal {
    private boolean value;

    public BoolLit(boolean value) {
        this.value = value;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    // Lombok generates isValue
    public boolean getValue() {
        return value;
    }
}
