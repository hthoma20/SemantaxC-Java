package com.symantax.ast.node;

import com.symantax.ast.util.FilePos;
import com.symantax.ast.visitor.ASTVisitor;
import lombok.Getter;

public class Word extends AstNode implements PhraseElement {

    @Getter
    private String value;

    public Word(FilePos filePos, String value) {
        super(filePos);
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }


    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
