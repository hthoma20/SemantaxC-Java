package com.semantax.ast.type;

import com.semantax.ast.node.list.NameTypePairList;
import com.semantax.ast.visitor.AstVisitor;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Builder(builderClassName = "Builder")
@Getter
public class FuncType extends Type {
    private final Type inputType;
    private final Optional<Type> outputType;

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
