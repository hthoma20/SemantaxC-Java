package com.semantax.ast.node;

import com.semantax.ast.type.Type;
import com.semantax.ast.util.eventual.Eventual;
import com.semantax.ast.visitor.AstVisitor;

public abstract class Expression extends AstNode implements PhraseElement {
    private final Eventual<Type> type = Eventual.unfulfilled();

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Type getType() {
        return type.get();
    }

    public void setType(Type type) {
        this.type.fulfill(type);
    }

    public boolean hasType() {
        return this.type.isFulfilled();
    }
}
