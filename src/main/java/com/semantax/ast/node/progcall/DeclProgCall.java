package com.semantax.ast.node.progcall;


import com.semantax.ast.node.VariableDeclaration;
import com.semantax.ast.node.list.ParsableExpressionList;
import com.semantax.ast.type.Type;
import com.semantax.ast.util.eventual.Eventual;
import com.semantax.ast.visitor.AstVisitor;
import lombok.Getter;

public class DeclProgCall extends ProgCall implements VariableDeclaration {

    private final Eventual<String> declName = Eventual.unfulfilled();
    private final Eventual<Type> declType = Eventual.unfulfilled();

    // Override builder class to allow for polymorphic building
    @lombok.Builder(builderClassName = "Builder")
    DeclProgCall(String name, ParsableExpressionList subExpressions) {
        super(name, subExpressions);
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public void setDeclName(String name) {
        this.declName.fulfill(name);
    }

    public void setDeclType(Type type) {
        this.declType.fulfill(type);
    }

    public boolean hasDeclName() {
        return declName.isFulfilled();
    }

    public boolean hasDeclType() {
        return declType.isFulfilled();
    }

    @Override
    public String getDeclName() {
        return declName.get();
    }

    @Override
    public Type getDeclType() {
        return declType.get();
    }

    public static class Builder extends ProgCall.Builder {}
}
