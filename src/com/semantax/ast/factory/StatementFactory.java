package com.semantax.ast.factory;

import com.semantax.ast.node.Expression;
import com.semantax.ast.node.Phrase;
import com.semantax.ast.node.Statement;
import com.semantax.ast.node.literal.IntLit;
import com.semantax.ast.util.FilePos;
import com.semantax.exception.UnexpectedTokenException;
import com.semantax.parser.generated.Token;

import static com.semantax.parser.generated.SymantaxParserConstants.DECIMAL_LIT;
import static com.semantax.parser.generated.SymantaxParserConstants.DOZENAL_LIT;

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
