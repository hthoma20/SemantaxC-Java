package com.semantax.exception;

import com.semantax.parser.generated.SemantaxParserConstants;
import com.semantax.parser.generated.Token;

public class UnexpectedTokenException extends RuntimeException {
    public UnexpectedTokenException(Token token) {
        super(String.format("Unexpected token %s: %s",
                SemantaxParserConstants.tokenImage[token.kind], token.image));
    }
}
