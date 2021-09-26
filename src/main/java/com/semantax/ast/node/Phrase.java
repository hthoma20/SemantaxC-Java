package com.semantax.ast.node;

import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.AstVisitor;
import com.semantax.exception.CompilerException;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;
import java.util.stream.Collectors;

@Builder(builderClassName = "Builder")
public class Phrase extends AstNode {
    @Getter
    @Singular("element")
    private List<PhraseElement> phrase;

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    /**
     * Filter out just the sub-expressions of this phrase,
     * this does not include any Identifier, since words will be mapped
     * to identifiers during the parsing of this Phrase
     *
     * For example, if this Phrase is
     * <code>
     *     let x = 4;
     * </code>
     * then the only subexpression is 4, so this method will return
     * a list with an IntLit 4
     *
     * @return all the subexpressions of this phrase
     */
    public List<Expression> subExpressions() {
        return phrase.stream()
                .filter(element -> element instanceof Expression)
                .map(exp -> (Expression) exp)
                .collect(Collectors.toList());
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

    @Override
    public String toString() {
        return String.format("Phrase(%s)", phrase.stream()
                .map(PhraseElement::toString)
                .collect(Collectors.joining(",")));
    }
}
