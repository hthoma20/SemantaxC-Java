package com.semantax.ast.factory;

import com.semantax.ast.util.FilePos;
import com.semantax.ast.node.literal.IntLit;
import com.semantax.exception.UnexpectedTokenException;
import com.semantax.parser.generated.Token;

import static com.semantax.parser.generated.SemantaxParserConstants.DECIMAL_LIT;
import static com.semantax.parser.generated.SemantaxParserConstants.DOZENAL_LIT;

public class IntLitFactory {

    public static IntLit fromToken(Token token) {
        FilePos filePos = new FilePos(token.beginLine, token.beginColumn);

        int intValue = getIntValue(token);
        IntLit intLit = new IntLit(intValue);
        intLit.setFilePos(filePos);

        return intLit;
    }

    private static int getIntValue(Token token) {
        String image = token.image;

        switch (token.kind) {
            case DECIMAL_LIT:
                return decimalLit(image);
            case DOZENAL_LIT:
                return dozenalLit(image);
            default:
                throw new UnexpectedTokenException(token);        }
    }

    private static int decimalLit(String image) {
        return Integer.parseInt(image);
    }

    private static int dozenalLit(String image) {
        return Integer.parseInt(image
                .toUpperCase()
                .substring(2)
                .replace('X', 'A')
                .replace('E', 'B'), 12);
    }
}
