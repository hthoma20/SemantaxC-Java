package com.semantax.ast.factory;

import com.semantax.ast.node.Word;
import com.semantax.ast.node.list.ParsableExpressionList;
import com.semantax.ast.node.progcall.DeclProgCall;
import com.semantax.ast.node.progcall.ProgCall;
import com.semantax.ast.util.FilePos;
import com.semantax.exception.UnexpectedTokenException;
import com.semantax.parser.generated.Token;

import static com.semantax.ast.node.progcall.ProgCallConstants.ADD_INT;
import static com.semantax.ast.node.progcall.ProgCallConstants.ADD_INT_BUILDER;
import static com.semantax.ast.node.progcall.ProgCallConstants.DECL;
import static com.semantax.ast.node.progcall.ProgCallConstants.PRINT_INT;
import static com.semantax.ast.node.progcall.ProgCallConstants.PRINT_INT_BUILDER;
import static com.semantax.ast.node.progcall.ProgCallConstants.PRINT_STRING;
import static com.semantax.ast.node.progcall.ProgCallConstants.PRINT_STRING_BUILDER;
import static com.semantax.parser.generated.SemantaxParserConstants.*;

public class ProgCallFactory {

    public static ProgCall from(Token at, Word name, ParsableExpressionList subExpressions) {

        validateToken(at);

        ProgCall.Builder builder = builderForName(name.getValue());

        return builder
                .name(name.getValue())
                .subExpressions(subExpressions)
                .buildWith(FilePos.from(at));
    }

    private static ProgCall.Builder builderForName(String name) {
        switch (name) {
            case DECL:
                return DeclProgCall.builder();
            case PRINT_INT:
                return PRINT_INT_BUILDER;
            case PRINT_STRING:
                return PRINT_STRING_BUILDER;
            case ADD_INT:
                return ADD_INT_BUILDER;
            default:
                return ProgCall.builder();
        }
    }

    private static void validateToken(Token token) {
        if (token.kind != AT) {
            throw new UnexpectedTokenException(token);
        }
    }
}
