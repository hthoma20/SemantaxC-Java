package com.semantax.exception;

import com.semantax.parser.generated.ParseException;
import com.semantax.parser.generated.Token;

public class CustomParseException extends ParseException {
    public CustomParseException(String message, Token currentToken) {
        super(message);
        this.currentToken = currentToken;
    }
}
