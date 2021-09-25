package com.semantax.ast.node;

import com.semantax.ast.util.FilePos;
import com.semantax.ast.util.eventual.Eventual;
import com.semantax.ast.util.eventual.FulfilledException;
import com.semantax.ast.visitor.ASTVisitor;
import lombok.Builder;

/**
 * Represents a Phrase or an Expression
 *
 * It will hold a Phrase until after Phrase parsing, at which point it will hold an expression
 */
@Builder(builderClassName = "Builder")
public class ParsableExpression extends AstNode implements PhraseElement {

    private final Phrase phrase;
    private final Eventual<Expression> expression = Eventual.unfulfilled();

    public static ParsableExpression fromPhrase(Phrase phrase) {
        return ParsableExpression.builder()
                .phrase(phrase)
                .build();
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Phrase getPhrase() {
        if (expression.isFulfilled()) {
            throw FulfilledException.of("Expression already fulfilled - cannot retrieve Phrase");
        }
        return phrase;
    }

    public Expression getExpression() {
        return expression.get();
    }

    public boolean hasExpression() {
        return expression.isFulfilled();
    }

    public void parseTo(Expression expression) {
        this.expression.fulfill(expression);
    }

    @Override
    public FilePos getFilePos() {
        return expression.isFulfilled() ? expression.get().getFilePos() : phrase.getFilePos();
    }

    @Override
    public String toString() {
        return String.format("%s(%s=%s)",
                getClass().getSimpleName(),
                hasExpression() ? "Expression" : "Phrase",
                hasExpression() ? getExpression() : getPhrase());
    }
}
