package com.semantax.ast.node.progcall;


import com.semantax.ast.node.list.ExpressionList;
import com.semantax.ast.type.Type;
import com.semantax.ast.util.eventual.Eventual;
import com.semantax.ast.visitor.ASTVisitor;
import lombok.Getter;

@Getter
public class DeclProgCall extends ProgCall {

    private final Eventual<String> declName = Eventual.unfulfilled();
    private final Eventual<Type> declType = Eventual.unfulfilled();

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
