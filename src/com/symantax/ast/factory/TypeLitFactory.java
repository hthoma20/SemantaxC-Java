package com.symantax.ast.factory;

import com.symantax.ast.node.*;
import com.symantax.ast.node.literal.type.*;
import com.symantax.ast.util.FilePos;
import com.symantax.exception.UnexpectedTokenException;
import com.symantax.parser.generated.Token;

import static com.symantax.parser.generated.SymantaxParserConstants.*;

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
