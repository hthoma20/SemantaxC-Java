package com.semantax.parser.generated;

import com.semantax.ast.node.Program;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static com.semantax.parser.generated.snapshot.SnapshotTestUtil.*;

class SymantaxParserTest {
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

    public void test_snapshot(String filename) throws FileNotFoundException, ParseException {
        SymantaxParser parser = getFileParser(String.format("././test_data/input/%s", filename));

        try {
            Program program = parser.Program();
            assertMatchesSnapshot(filename, program);
        }
        catch (ParseException parseException) {
            System.err.printf("Encountered ParseException while parsing %s%n", filename);
            throw parseException;
        }
    }

    @Test
    public void test_allSnapshots() throws FileNotFoundException, ParseException {
        File testData = new File("./test_data/input");

        for (String fileName : testData.list()) {
            test_snapshot(fileName);
        }
    }


    @Test
    public void test_InvalidFuncTypeLit() {
        SymantaxParser parser = getStringParser("func{int string}");
        assertThrows(ParseException.class, () -> parser.FuncTypeLit());
    }
}