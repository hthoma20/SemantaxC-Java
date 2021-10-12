package com.semantax.ast.node.progcall;


import com.semantax.ast.node.list.ParsableExpressionList;
import com.semantax.ast.node.literal.IntLit;
import com.semantax.ast.util.eventual.Eventual;
import com.semantax.ast.visitor.AstVisitor;
import lombok.Getter;

@Getter
public class AddIntProgCall extends ProgCall {

    private final Eventual<IntLit> lhs = Eventual.unfulfilled();
    private final Eventual<IntLit> rhs = Eventual.unfulfilled();

    // Override builder class to allow for polymorphic building
    @lombok.Builder(builderClassName = "Builder")
    AddIntProgCall(String name, ParsableExpressionList subExpressions) {
        super(name, subExpressions);
    }


    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public static class Builder extends ProgCall.Builder {}
}
