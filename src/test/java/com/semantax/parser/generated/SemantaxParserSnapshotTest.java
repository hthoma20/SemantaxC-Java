package com.semantax.parser.generated;

import com.semantax.ast.node.Program;
import com.semantax.parser.generated.snapshot.SnapshotTestUtil;
import lombok.RequiredArgsConstructor;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
@RequiredArgsConstructor
public class SemantaxParserSnapshotTest {

    private static final String TEST_DATA_ROOT = "./src/test/resources/test_data";
    private SnapshotTestUtil snapshotTestUtil = new SnapshotTestUtil(TEST_DATA_ROOT);

    // Parameter for test
    private final String testFile;

    /**
     * @param filePath path of the file to parse, absolute or relative to the content root
     * @return a parser for the contents of the given file
     */
    private SemantaxParser getFileParser(String filePath) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(filePath);
        return new SemantaxParser(fis, StandardCharsets.UTF_8);
    }

    @Test
    public void test_snapshot() throws FileNotFoundException, ParseException {
        SemantaxParser parser = getFileParser(String.format("%s/input/%s", TEST_DATA_ROOT, testFile));

        try {
            Program program = parser.Program();
            snapshotTestUtil.assertMatchesSnapshot(testFile, program);
        }
        catch (ParseException parseException) {
            System.err.printf("Encountered ParseException while parsing %s%n", testFile);
            throw parseException;
        }
    }

    @Parameterized.Parameters
    public static Collection<String> testFiles() {
        return Arrays.asList("arrays.smtx",
                "functions.smtx",
                "modules.smtx",
                "phrases.smtx",
                "progcalls.smtx",
                "records.smtx",
                "strings.smtx",
                "types.smtx");
    }
}