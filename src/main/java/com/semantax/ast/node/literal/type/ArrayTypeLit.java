package com.semantax.ast.node.literal.type;

import com.semantax.ast.visitor.AstVisitor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public class ArrayTypeLit extends TypeLit {

    @Getter
    @Setter
    TypeLit subType;

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
