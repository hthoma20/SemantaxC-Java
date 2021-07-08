package com.semantax.ast.factory;

import com.semantax.ast.util.FilePos;
import com.semantax.ast.node.literal.BoolLit;
import com.semantax.exception.UnexpectedTokenException;
import com.semantax.parser.generated.Token;

import static com.semantax.parser.generated.SemantaxParserConstants.*;

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
                throw new UnexpectedTokenException(token);
        }
    }
}
