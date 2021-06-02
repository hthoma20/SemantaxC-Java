package com.symantax.ast.node.literal;

import com.symantax.ast.node.Expression;
import com.symantax.ast.visitor.ASTVisitor;
import lombok.Getter;
import lombok.ToString;

@ToString
public class IntLit extends Literal {
    @Getter
    private int value;

    public IntLit(int value) {
        this.value = value;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
