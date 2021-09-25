package com.semantax.parser.generated;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.semantax.testutil.ParserUtil.getStringParser;
import static org.junit.Assert.assertThrows;

public class SemantaxParserErrorTest extends TestCase {

    @Test
    public void test_InvalidFuncTypeLit() {
        SemantaxParser parser = getStringParser("func{int string}");
        assertThrows(ParseException.class, () -> parser.FuncTypeLit());
    }
}
