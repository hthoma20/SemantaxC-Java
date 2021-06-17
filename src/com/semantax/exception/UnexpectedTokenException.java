package com.semantax.exception;

import com.semantax.parser.generated.SymantaxParserConstants;
import com.semantax.parser.generated.Token;

public class UnexpectedTokenException extends RuntimeException {
    public UnexpectedTokenException(Token token) {
        super(String.format("Unexpected token %s: %s",
                SymantaxParserConstants.tokenImage[token.kind], token.image));
    }
}
