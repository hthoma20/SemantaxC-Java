package com.semantax.main.args;

import com.semantax.logger.ErrorLogger;
import junit.framework.TestCase;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.mockito.Mockito.mock;

@RunWith(Parameterized.class)
@RequiredArgsConstructor
public class SemantaxCArgParserTest extends TestCase {

    private final TestData testData; // Parameterized data, set in RequiredArgsConstructor

    ErrorLogger errorLogger = mock(ErrorLogger.class);
    SemantaxCArgParser argParser = new SemantaxCArgParser(errorLogger);

    @Test
    public void test_argParser() {
        Optional<SemantaxCArgs> args = argParser.parse(testData.args);
        assertTrue(args.isPresent());
        assertEquals(testData.expectedArgs, args.get());
    }

    @Parameterized.Parameters
    public static Collection<TestData> testData() {
        return Arrays.asList(
                new TestData(new String[]{"prog.smtx"}, SemantaxCArgs.builder()
                                        .inputFiles(Arrays.asList("prog.smtx"))
                                        .outputFile("out.cpp")
                                        .astFile(Optional.empty())
                                        .enableBreadCrumbs(false)
                                        .build()),
                new TestData(new String[]{"prog.smtx", "-c", "thing.cpp"}, SemantaxCArgs.builder()
                        .inputFiles(Arrays.asList("prog.smtx"))
                        .outputFile("thing.cpp")
                        .astFile(Optional.empty())
                        .enableBreadCrumbs(false)
                        .build()),
                new TestData(new String[]{"prog.smtx", "-a"}, SemantaxCArgs.builder()
                        .inputFiles(Arrays.asList("prog.smtx"))
                        .outputFile("out.cpp")
                        .astFile(Optional.empty())
                        .enableBreadCrumbs(true)
                        .build()),
                new TestData(new String[]{"prog.smtx", "-a", "-c", "thing.cpp"}, SemantaxCArgs.builder()
                        .inputFiles(Arrays.asList("prog.smtx"))
                        .outputFile("thing.cpp")
                        .astFile(Optional.empty())
                        .enableBreadCrumbs(true)
                        .build()),
                new TestData(new String[]{"prog.smtx", "--ast", "ast.txt", "-c", "thing.cpp"}, SemantaxCArgs.builder()
                        .inputFiles(Arrays.asList("prog.smtx"))
                        .outputFile("thing.cpp")
                        .astFile(Optional.of("ast.txt"))
                        .enableBreadCrumbs(false)
                        .build())
        );
    }

    @AllArgsConstructor
    private static class TestData {
        String[] args;
        SemantaxCArgs expectedArgs;
    }
}