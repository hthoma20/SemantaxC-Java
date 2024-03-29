package com.semantax.ast.factory;

import com.semantax.ast.node.ParsableExpression;
import com.semantax.ast.node.Phrase;
import com.semantax.ast.node.Statement;

public class StatementFactory {

    public static Statement fromPhrase(Phrase phrase) {
        return Statement.builder()
                .parsableExpression(ParsableExpression.fromPhrase(phrase))
                .buildWith(phrase.getFilePos());
    }
}
