package com.semantax.ast.node;

import com.semantax.ast.visitor.ASTVisitor;
import lombok.Builder;

import java.util.List;

@Builder
public class Phrase extends Expression {
    private List<PhraseElement> phrase;

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
