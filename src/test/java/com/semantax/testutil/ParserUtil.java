package com.semantax.testutil;

import com.semantax.parser.generated.SemantaxParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ParserUtil {
    /**
     * @param input the string to parse
     * @return a parser for the given string
     */
    public static SemantaxParser getStringParser(String input) {
        InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        return new SemantaxParser(stream, StandardCharsets.UTF_8);
    }
}
