package com.semantax.ast.factory;

import com.semantax.ast.node.Word;
import com.semantax.ast.node.list.ParsableExpressionList;
import com.semantax.ast.node.progcall.BindProgCall;
import com.semantax.ast.node.progcall.DeclProgCall;
import com.semantax.ast.node.progcall.ProgCall;
import com.semantax.ast.node.progcall.ReturnProgCall;
import com.semantax.ast.util.FilePos;
import com.semantax.exception.UnexpectedTokenException;
import com.semantax.parser.generated.Token;

import static com.semantax.ast.node.progcall.ProgCallConstants.ADD_INT;
import static com.semantax.ast.node.progcall.ProgCallConstants.ADD_INT_BUILDER;
import static com.semantax.ast.node.progcall.ProgCallConstants.ARRAY_GET;
import static com.semantax.ast.node.progcall.ProgCallConstants.ARRAY_GET_BUILDER;
import static com.semantax.ast.node.progcall.ProgCallConstants.ARRAY_LEN;
import static com.semantax.ast.node.progcall.ProgCallConstants.ARRAY_LEN_BUILDER;
import static com.semantax.ast.node.progcall.ProgCallConstants.ARRAY_SET;
import static com.semantax.ast.node.progcall.ProgCallConstants.ARRAY_SET_BUILDER;
import static com.semantax.ast.node.progcall.ProgCallConstants.BIND;
import static com.semantax.ast.node.progcall.ProgCallConstants.DECL;
import static com.semantax.ast.node.progcall.ProgCallConstants.INIT_ARRAY;
import static com.semantax.ast.node.progcall.ProgCallConstants.INIT_ARRAY_BUILDER;
import static com.semantax.ast.node.progcall.ProgCallConstants.INVOKE_FUN;
import static com.semantax.ast.node.progcall.ProgCallConstants.INVOKE_FUN_BUILDER;
import static com.semantax.ast.node.progcall.ProgCallConstants.PRINT_INT;
import static com.semantax.ast.node.progcall.ProgCallConstants.PRINT_INT_BUILDER;
import static com.semantax.ast.node.progcall.ProgCallConstants.PRINT_STRING;
import static com.semantax.ast.node.progcall.ProgCallConstants.PRINT_STRING_BUILDER;
import static com.semantax.ast.node.progcall.ProgCallConstants.RETURN;
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
            case BIND:
                return BindProgCall.builder();
            case RETURN:
                return ReturnProgCall.builder();
            case PRINT_INT:
                return PRINT_INT_BUILDER;
            case PRINT_STRING:
                return PRINT_STRING_BUILDER;
            case ADD_INT:
                return ADD_INT_BUILDER;
            case INVOKE_FUN:
                return INVOKE_FUN_BUILDER;
            case ARRAY_GET:
                return ARRAY_GET_BUILDER;
            case ARRAY_SET:
                return ARRAY_SET_BUILDER;
            case ARRAY_LEN:
                return ARRAY_LEN_BUILDER;
            case INIT_ARRAY:
                return INIT_ARRAY_BUILDER;
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
