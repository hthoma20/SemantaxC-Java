package com.symantax.ast.node.literal;

import com.symantax.ast.node.Expression;
import com.symantax.ast.visitor.ASTVisitor;
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
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
