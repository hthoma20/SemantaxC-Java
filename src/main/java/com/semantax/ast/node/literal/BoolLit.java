package com.semantax.ast.node.literal;

import com.semantax.ast.visitor.AstVisitor;
import lombok.ToString;

@ToString
public class BoolLit extends Literal {
    private boolean value;

    public BoolLit(boolean value) {
        this.value = value;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    // Lombok generates isValue
    public boolean getValue() {
        return value;
    }
}
