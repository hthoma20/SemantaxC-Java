package com.semantax.ast.node.progcall;

import com.semantax.ast.node.Expression;
import com.semantax.ast.node.VariableReference;
import com.semantax.ast.node.list.ParsableExpressionList;
import com.semantax.ast.util.eventual.Eventual;
import com.semantax.ast.visitor.AstVisitor;

public class BindProgCall extends StaticProgCall {

    private final Eventual<VariableReference> variableReference = Eventual.unfulfilled();
    private final Eventual<Expression> value = Eventual.unfulfilled();

    // Override builder class to allow for polymorphic building
    @lombok.Builder(builderClassName = "Builder")
    BindProgCall(String name, ParsableExpressionList subExpressions) {
        super(name, subExpressions);
    }
    public static class Builder extends StaticProgCall.Builder {}

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public void setVariableReference(VariableReference variableReference) {
        this.variableReference.fulfill(variableReference);
    }

    public void setValue(Expression value) {
        this.value.fulfill(value);
    }

    public VariableReference getVariableReference() {
        return variableReference.get();
    }

    public Expression getValue() {
        return value.get();
    }
}
