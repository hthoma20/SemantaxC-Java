package com.symantax.ast.util;

import lombok.Value;

@Value
public class FilePos {
    private int line;
    private int column;

    @Override
    public String toString() {
        return String.format("%s:%s", line, column);
    }
}
