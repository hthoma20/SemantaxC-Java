package com.symantax.ast.node.literal.type;

import com.symantax.ast.type.Type;
import com.symantax.ast.visitor.ASTVisitor;
import lombok.Getter;
import lombok.Setter;

public class ArrayTypeLit extends TypeLit {

    @Getter
    @Setter
    Type subType;

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
