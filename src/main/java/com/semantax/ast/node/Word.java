package com.semantax.ast.node;

import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.AstVisitor;
import com.semantax.parser.generated.Token;
import lombok.Getter;

public class Word extends AstNode implements PhraseElement {

    @Getter
    private String value;

    public Word(FilePos filePos, String value) {
        super(filePos);
        this.value = value;
    }

    public static Word fromToken(Token t) {
        return new Word(FilePos.from(t), t.image);
    }

    @Override
    public String toString() {
        return value;
    }


    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
