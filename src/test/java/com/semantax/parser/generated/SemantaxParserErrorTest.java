package com.semantax.parser.generated;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertThrows;

public class SemantaxParserErrorTest extends TestCase {

    /**
     * @param input the string to parse
     * @return a parser for the given string
     */
    private SemantaxParser getStringParser(String input) {
        InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        return new SemantaxParser(stream, StandardCharsets.UTF_8);
    }

    @Test
    public void test_InvalidFuncTypeLit() {
        SemantaxParser parser = getStringParser("func{int string}");
        assertThrows(ParseException.class, () -> parser.FuncTypeLit());
    }
}
