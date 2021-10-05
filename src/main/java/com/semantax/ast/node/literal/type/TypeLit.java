package com.semantax.ast.node.literal.type;

import com.semantax.ast.node.literal.Literal;
import com.semantax.ast.type.Type;
import com.semantax.ast.util.eventual.Eventual;
import com.semantax.ast.visitor.AstVisitor;

public abstract class TypeLit extends Literal {
    // the type that this literal represents
    private final Eventual<Type> representedType = Eventual.unfulfilled();

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Type getRepresentedType() {
        return representedType.get();
    }

    public void setRepresentedType(Type type) {
        representedType.fulfill(type);
    }
}
