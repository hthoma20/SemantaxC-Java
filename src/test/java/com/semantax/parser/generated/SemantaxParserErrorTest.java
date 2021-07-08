package com.semantax.parser.generated;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertThrows;

public class SemantaxParserErrorTest {

    /**
     * @param input the string to parse
     * @return a parser for the given string
     */
    private SymantaxParser getStringParser(String input) {
        InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        return new SymantaxParser(stream);
    }

    @Test
    public void test_InvalidFuncTypeLit() {
        SymantaxParser parser = getStringParser("func{int string}");
        assertThrows(ParseException.class, () -> parser.FuncTypeLit());
    }
}
