package com.symantax.parser.generated;

import com.symantax.ast.node.Program;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static com.symantax.parser.generated.snapshot.SnapshotTestUtil.*;

class SymantaxParserTest {

    public void test_snapshot(String filename) throws FileNotFoundException, ParseException {
        FileInputStream fis = new FileInputStream(String.format("././test_data/input/%s", filename));
        SymantaxParser parser = new SymantaxParser(fis);

        try {
            Program program = parser.Program();
            assertMatchesSnapshot(filename, program);
        }
        catch (ParseException parseException) {
            System.err.printf("Encountered ParseException while parsing %s%n", filename);
            throw parseException;
        }
    }

    private static String[] getTestData() {
        return new File("./test_data/input").list();
    }

    @Test
    public void test_allSnapshots() throws FileNotFoundException, ParseException {
        File testData = new File("./test_data/input");

        for (String fileName : testData.list()) {
            test_snapshot(fileName);
        }
    }
}