package com.symantax.ast.node;

import com.symantax.ast.node.list.ExpressionList;
import com.symantax.ast.visitor.ASTVisitor;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "Builder")
@Getter
public class ProgCall extends Expression {
    private String name;
    private ExpressionList subExpressions;

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
