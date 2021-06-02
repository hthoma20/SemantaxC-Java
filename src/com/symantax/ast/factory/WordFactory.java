package com.symantax.ast.factory;

import com.symantax.ast.node.Word;
import com.symantax.ast.util.FilePos;
import com.symantax.exception.UnexpectedTokenException;
import com.symantax.parser.generated.Token;

import static com.symantax.parser.generated.SymantaxParserConstants.*;

public class WordFactory {
    public static Word fromToken(Token token) {
        FilePos filePos = new FilePos(token.beginLine, token.beginColumn);

        switch (token.kind) {
            case WORD:
                return new Word(filePos, token.image);
            default:
                throw new UnexpectedTokenException(token);
        }
    }
}
