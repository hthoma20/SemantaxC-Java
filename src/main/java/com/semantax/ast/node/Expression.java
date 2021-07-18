package com.semantax.ast.node;

import com.semantax.ast.type.Type;
import com.semantax.ast.visitor.ASTVisitor;
import lombok.Getter;
import lombok.Setter;

public abstract class Expression extends AstNode implements PhraseElement {
    @Getter
    @Setter
    private Type type;

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}