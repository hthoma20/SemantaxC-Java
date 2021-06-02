package com.symantax.ast.node;

import com.symantax.ast.node.list.WordList;
import com.symantax.ast.visitor.ASTVisitor;
import lombok.Builder;

@Builder
public class PatternInvocation extends Expression {
    private PatternDefinition patternDefinition;

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
