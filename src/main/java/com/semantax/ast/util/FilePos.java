package com.semantax.ast.util;

import com.semantax.parser.generated.Token;
import lombok.Value;

@Value
public class FilePos implements Comparable<FilePos> {
    private int line;
    private int column;

    public static FilePos from(Token token) {
        return new FilePos(token.beginLine, token.beginColumn);
    }

    @Override
    public String toString() {
        return String.format("%s:%s", line, column);
    }

    /**
     * Compare this FilePos to another
     *
     * @param o the FilePos to compare this FilePos to
     * @return a negative number if this FilePos is before o, a positive number if after,
     *          and 0 if they represent the same file position
     */
    @Override
    public int compareTo(FilePos o) {
        if (this.line != o.line) {
            return this.line - o.line;
        }

        return this.column - o.column;
    }
}
