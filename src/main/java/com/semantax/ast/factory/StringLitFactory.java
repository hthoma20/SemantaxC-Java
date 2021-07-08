package com.semantax.ast.factory;

import com.semantax.ast.util.FilePos;
import com.semantax.ast.node.literal.StringLit;
import com.semantax.exception.UnexpectedTokenException;
import com.semantax.parser.generated.Token;

import static com.semantax.parser.generated.SymantaxParserConstants.DOUBLE_QUOTED_STRING;
import static com.semantax.parser.generated.SymantaxParserConstants.SINGLE_QUOTED_STRING;

public class StringLitFactory {

    public static StringLit fromToken(Token token) {

        validateToken(token);

        FilePos filePos = new FilePos(token.beginLine, token.beginColumn);

        String value = getStringValue(token);
        StringLit stringLit = new StringLit(value);
        stringLit.setFilePos(filePos);

        return stringLit;
    }

    private static String getStringValue(Token token) {
        String image = token.image;

        // Shave off the delimiting quotes
        return image.substring(1, image.length()-1);
    }


    private static void validateToken(Token token) {
        switch(token.kind) {
            case DOUBLE_QUOTED_STRING:
            case SINGLE_QUOTED_STRING:
                return;
            default:
                throw new UnexpectedTokenException(token);
        }
    }
}
