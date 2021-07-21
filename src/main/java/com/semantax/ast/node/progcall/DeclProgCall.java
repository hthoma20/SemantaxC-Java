package com.semantax.ast.node.progcall;

import com.semantax.ast.node.list.ExpressionList;
import com.semantax.ast.visitor.ASTVisitor;

public class DeclProgCall extends ProgCall {

    @lombok.Builder(builderClassName = "Builder")
    DeclProgCall(String name, ExpressionList subExpressions) {
        super(name, subExpressions);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public static class Builder extends ProgCall.Builder {}
}
