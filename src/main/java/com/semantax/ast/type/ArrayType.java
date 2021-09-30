package com.semantax.ast.type;

import com.semantax.ast.visitor.AstVisitor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder(builderClassName = "Builder")
public class ArrayType extends Type {

    public static final ArrayType EMPTY_TYPE = ArrayType.builder()
            .subType(VoidType.VOID_TYPE)
            .build();

    @NonNull
    @Getter
    private final Type subType;

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
