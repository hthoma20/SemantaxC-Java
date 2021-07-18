package com.semantax.ast.util;

import com.semantax.parser.generated.Token;
import junit.framework.TestCase;

public class FilePosTest extends TestCase {

    public void test_from() {
        int beginLine = 42;
        int beginColumn = 12;

        Token token = new Token();
        token.beginLine = beginLine;
        token.beginColumn = beginColumn;

        FilePos filePos = FilePos.from(token);

        assertEquals(beginLine, filePos.getLine());
        assertEquals(beginColumn, filePos.getColumn());
    }

    public void test_compareTo() {
        FilePos pos10_4 = new FilePos(10, 4);
        FilePos pos10_10 = new FilePos(10, 10);
        FilePos pos20_4 = new FilePos(20, 4);

        assertTrue(pos10_4.compareTo(pos10_10) < 0);
        assertTrue(pos10_4.compareTo(pos20_4) < 0);
        assertTrue(pos10_10.compareTo(pos20_4) < 0);
        assertEquals(0, pos10_10.compareTo(pos10_10));
    }
}
