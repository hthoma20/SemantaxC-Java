package com.semantax.parser.generated;

import com.semantax.ast.node.Program;
import com.semantax.testutil.SnapshotTestUtil;
import junit.framework.TestCase;
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
public class SemantaxParserSnapshotTest extends TestCase {

    private static final String INPUT_DIR = "./src/test/resources/test_data/grammar/input";
    private static final String SNAPSHOT_DIR = "./src/test/resources/test_data/grammar/snapshots";
    private final SnapshotTestUtil snapshotTestUtil = new SnapshotTestUtil(SNAPSHOT_DIR);

    // Parameter for test, populated in constructor
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
        SemantaxParser parser = getFileParser(String.format("%s/%s", INPUT_DIR, testFile));

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
        return Arrays.asList(
                new File(INPUT_DIR).list());
    }
}