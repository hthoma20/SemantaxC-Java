package com.semantax.ast.factory;

import com.semantax.ast.node.Phrase;
import com.semantax.ast.node.Statement;
import com.semantax.ast.node.Expression;

public class StatementFactory {

    public static Statement fromExpression(Expression expression) {
        return Statement.builder()
                .phrase(Phrase.builder().element(expression).build())
                .buildWith(expression.getFilePos());
    }

    public static Statement fromPhrase(Phrase phrase) {
        return Statement.builder()
                .phrase(phrase)
                .buildWith(phrase.getFilePos());
    }
}
