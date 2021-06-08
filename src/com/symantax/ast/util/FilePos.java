package com.symantax.ast.util;

import com.symantax.parser.generated.Token;
import lombok.Value;

@Value
public class FilePos {
    private int line;
    private int column;

    public static FilePos from(Token token) {
        return new FilePos(token.beginLine, token.beginColumn);
    }

    @Override
    public String toString() {
        return String.format("%s:%s", line, column);
    }
}
