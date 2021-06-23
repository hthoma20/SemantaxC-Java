package com.semantax.ast.factory;

import com.semantax.ast.node.Expression;
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
                .expression(expression)
                .buildWith(expression.getFilePos());
    }
}
