package com.semantax.parser.generated;

import com.semantax.ast.node.Program;
import com.semantax.parser.generated.snapshot.SnapshotTestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class SemantaxParserTest {

    private static final String TEST_DATA_ROOT = "./tst/test_data";
    private SnapshotTestUtil snapshotTestUtil = new SnapshotTestUtil(TEST_DATA_ROOT);

    /**
     * @param input the string to parse
     * @return a parser for the given string
     */
    private SymantaxParser getStringParser(String input) {
        InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        return new SymantaxParser(stream);
    }

    /**
     * @param filePath path of the file to parse, absolute or relative to the content root
     * @return a parser for the contents of the given file
     */
    private SymantaxParser getFileParser(String filePath) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(filePath);
        return new SymantaxParser(fis);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "arrays.smtx",
            "functions.smtx",
            "modules.smtx",
            "phrases.smtx",
            "progcalls.smtx",
            "records.smtx",
            "strings.smtx",
            "types.smtx"
    })
    public void test_snapshot(String filename) throws FileNotFoundException, ParseException {
        SymantaxParser parser = getFileParser(String.format("%s/input/%s", TEST_DATA_ROOT, filename));

        try {
            Program program = parser.Program();
            snapshotTestUtil.assertMatchesSnapshot(filename, program);
        }
        catch (ParseException parseException) {
            System.err.printf("Encountered ParseException while parsing %s%n", filename);
            throw parseException;
        }
    }

    @Test
    public void test_InvalidFuncTypeLit() {
        SymantaxParser parser = getStringParser("func{int string}");
        assertThrows(ParseException.class, () -> parser.FuncTypeLit());
    }
}