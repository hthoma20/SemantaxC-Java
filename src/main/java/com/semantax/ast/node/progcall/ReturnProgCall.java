package com.semantax.ast.node.progcall;

import com.semantax.ast.node.Expression;
import com.semantax.ast.node.VariableReference;
import com.semantax.ast.node.list.ParsableExpressionList;
import com.semantax.ast.util.eventual.Eventual;
import com.semantax.ast.visitor.AstVisitor;

public class ReturnProgCall extends StaticProgCall {

    private final Eventual<Expression> returnExpression = Eventual.unfulfilled();

    // Override builder class to allow for polymorphic building
    @lombok.Builder(builderClassName = "Builder")
    ReturnProgCall(String name, ParsableExpressionList subExpressions) {
        super(name, subExpressions);
    }
    public static class Builder extends StaticProgCall.Builder {}

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public void setReturnExpression(Expression returnExpression) {
        this.returnExpression.fulfill(returnExpression);
    }

    public Expression getReturnExpression() {
        return this.returnExpression.get();
    }
}
