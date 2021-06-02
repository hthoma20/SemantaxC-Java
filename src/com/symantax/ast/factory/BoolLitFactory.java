package com.symantax.ast.factory;

import com.symantax.ast.node.literal.BoolLit;
import com.symantax.ast.util.FilePos;
import com.symantax.exception.UnexpectedTokenException;
import com.symantax.parser.generated.Token;

import static com.symantax.parser.generated.SymantaxParserConstants.*;

public class BoolLitFactory {

    public static BoolLit fromToken(Token token) {
        FilePos filePos = new FilePos(token.beginLine, token.beginColumn);

        boolean booleanValue = getBooleanValue(token);
        BoolLit boolLit = new BoolLit(booleanValue);
        boolLit.setFilePos(filePos);

        return boolLit;
    }

    private static boolean getBooleanValue(Token token) {
        String image = token.image;

        switch (token.kind) {
            case FALSE:
                return false;
            case TRUE:
                return true;
            default:
                throw new UnexpectedTokenException(token);        }
    }
}
