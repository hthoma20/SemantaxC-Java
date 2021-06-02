package com.symantax.ast.factory;

import com.symantax.ast.node.literal.StringLit;
import com.symantax.ast.util.FilePos;
import com.symantax.exception.UnexpectedTokenException;
import com.symantax.parser.generated.Token;

import static com.symantax.parser.generated.SymantaxParserConstants.*;

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
