package com.semantax.ast.node.literal;

import com.semantax.ast.visitor.AstVisitor;
import lombok.Getter;
import lombok.ToString;

@ToString
public class StringLit extends Literal {
    @Getter
    private String value;

    public StringLit(String value) {
        this.value = value;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
