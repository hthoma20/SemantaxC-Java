package com.semantax.ast.node;

import com.semantax.ast.visitor.AstVisitor;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "Builder")
@Getter
public class VariableReference extends Expression {
    private final VariableDeclaration declaration;

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
