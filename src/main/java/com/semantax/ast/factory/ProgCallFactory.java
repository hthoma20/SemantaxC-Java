package com.semantax.ast.factory;

import com.semantax.ast.node.Word;
import com.semantax.ast.node.list.ExpressionList;
import com.semantax.ast.node.progcall.ProgCall;
import com.semantax.ast.util.FilePos;
import com.semantax.exception.UnexpectedTokenException;
import com.semantax.parser.generated.Token;

import static com.semantax.parser.generated.SemantaxParserConstants.*;

public class ProgCallFactory {

    public static ProgCall from(Token at, Word name, ExpressionList subExpressions) {

        validateToken(at);

        return ProgCall.builder()
                .name(name.getValue())
                .subExpressions(subExpressions)
                .buildWith(FilePos.from(at));
    }

    private static void validateToken(Token token) {
        if (token.kind != AT) {
            throw new UnexpectedTokenException(token);
        }
    }
}
