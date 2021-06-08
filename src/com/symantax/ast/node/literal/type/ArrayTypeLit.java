package com.symantax.ast.node.literal.type;

import com.symantax.ast.visitor.ASTVisitor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public class ArrayTypeLit extends TypeLit {

    @Getter
    @Setter
    TypeLit subType;

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
