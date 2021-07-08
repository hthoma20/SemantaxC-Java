package com.semantax.ast.factory;

import com.semantax.ast.node.Word;
import com.semantax.ast.util.FilePos;
import com.semantax.exception.UnexpectedTokenException;
import com.semantax.parser.generated.Token;

import static com.semantax.parser.generated.SymantaxParserConstants.WORD;

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
