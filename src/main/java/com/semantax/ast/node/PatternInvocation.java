package com.semantax.ast.node;

import com.semantax.ast.visitor.AstVisitor;
import lombok.Builder;

@Builder
public class PatternInvocation extends Expression {
    private PatternDefinition patternDefinition;

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
