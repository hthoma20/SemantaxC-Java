package com.semantax.ast.factory;

import com.semantax.ast.node.literal.type.*;
import com.semantax.ast.util.FilePos;
import com.semantax.exception.UnexpectedTokenException;
import com.semantax.parser.generated.Token;

import static com.semantax.parser.generated.SemantaxParserConstants.*;

public class TypeLitFactory {

    public static TypeLit fromToken(Token token) {
        FilePos filePos = new FilePos(token.beginLine, token.beginColumn);

        TypeLit typeLit = getTypeLit(token);
        typeLit.setFilePos(filePos);

        return typeLit;
    }

    private static TypeLit getTypeLit(Token token) {
        String image = token.image;

        switch (token.kind) {
            case TYPE:
                return new TypeTypeLit();
            case INT:
                return new IntTypeLit();
            case STRING:
                return new StringTypeLit();
            case BOOL:
                return new BoolTypeLit();
            default:
                throw new UnexpectedTokenException(token);
        }
    }

}
