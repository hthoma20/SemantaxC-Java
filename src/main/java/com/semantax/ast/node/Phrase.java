package com.semantax.ast.node;

import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.ASTVisitor;
import com.semantax.exception.CompilerException;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Builder(builderClassName = "Builder")
public class Phrase extends Expression {
    @Getter
    @Singular("element")
    private List<PhraseElement> phrase;

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public FilePos getFilePos() {
        return phrase.stream()
                .filter(element -> element instanceof AstNode)
                .map(node -> (AstNode) node)
                .findAny()
                .map(AstNode::getFilePos)
                .orElseThrow(() -> new CompilerException("Phrase has no element which is an AstNode"));
    }
}
