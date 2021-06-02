package com.symantax.exception;

import com.symantax.parser.generated.SymantaxParserConstants;
import com.symantax.parser.generated.Token;

public class UnexpectedTokenException extends RuntimeException {
    public UnexpectedTokenException(Token token) {
        super(String.format("Unexpected token %s: %s",
                SymantaxParserConstants.tokenImage[token.kind], token.image));
    }
}
