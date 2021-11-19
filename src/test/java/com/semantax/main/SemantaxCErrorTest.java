package com.semantax.main;

import com.semantax.ast.node.AstNode;
import com.semantax.ast.node.Program;
import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.AstPrintingVisitor;
import com.semantax.error.ErrorType;
import com.semantax.logger.ErrorLogger;
import com.semantax.main.args.SemantaxCArgs;
import com.semantax.parser.generated.ParseException;
import com.semantax.parser.generated.SemantaxParser;
import com.semantax.phase.GrammarPhase;
import com.semantax.phase.ParsePhase;
import com.semantax.phase.annotator.DefaultTypeAnnotator;
import com.semantax.phase.annotator.DefaultTypeAssignabilityChecker;
import com.semantax.phase.annotator.RecordTypeUtil;
import com.semantax.phase.annotator.TypeAnnotator;
import com.semantax.phase.annotator.TypeAssignabilityChecker;
import com.semantax.phase.parser.DefaultPhraseParser;
import com.semantax.phase.parser.PatternUtil;
import com.semantax.phase.parser.PhraseParser;
import com.semantax.testutil.SnapshotTestUtil;
import junit.framework.TestCase;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

import static com.semantax.testutil.AssertionUtil.matchesPred;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(Parameterized.class)
@RequiredArgsConstructor
public class SemantaxCErrorTest extends TestCase {
    private static final String INPUT_DIR = "./src/test/resources/test_data/error";

    // Parameter for test, populated in constructor
    private final ErrorTestData testData;

    TypeAssignabilityChecker typeAssignabilityChecker = new DefaultTypeAssignabilityChecker();
    PatternUtil patternUtil = new PatternUtil(typeAssignabilityChecker);
    RecordTypeUtil recordTypeUtil = new RecordTypeUtil();
    ErrorLogger errorLogger = mock(ErrorLogger.class);

    PhraseParser phraseParser = new DefaultPhraseParser(patternUtil);
    TypeAnnotator typeAnnotator = new DefaultTypeAnnotator(typeAssignabilityChecker, recordTypeUtil, errorLogger);

    ParsePhase parsePhase = new ParsePhase(errorLogger, phraseParser, typeAnnotator);
    GrammarPhase grammarPhase = new GrammarPhase(errorLogger);

    AstPrintingVisitor astPrintingVisitor = mock(AstPrintingVisitor.class);

    // subject
    SemantaxC semantaxC = new SemantaxC(astPrintingVisitor, errorLogger, grammarPhase, parsePhase);


    @Test
    public void test_errors() {
        SemantaxCArgs args = SemantaxCArgs.builder()
                .inputFile(String.format("%s/%s", INPUT_DIR, testData.file))
                .build();

        semantaxC.execute(args);

        verify(errorLogger).error(matchesPred(testData.expectedErrorType), matchesPred(testData.expectedErrorFilePos),
                any(), anyVararg());
    }

    @Parameterized.Parameters
    public static Collection<ErrorTestData> testFiles() {
        return Arrays.asList(
                testData("missingMainModule.smtx", ErrorType.MISSING_MAIN_MODULE),
                testData("twoMainModules.smtx", ErrorType.MULTIPLE_MAIN_MODULES),
                testData("patternMismatchedBrackets.smtx", ErrorType.PROGRAM_PARSE_ERROR, new FilePos(2, 20)));
    }

    private static ErrorTestData testData(String file, ErrorType errorType) {
        return ErrorTestData.builder()
                .file(file)
                .expectedErrorType(errorType::equals)
                .expectedErrorFilePos(filePos -> true)
                .build();
    }

    private static ErrorTestData testData(String file, ErrorType errorType, FilePos filePos) {
        return ErrorTestData.builder()
                .file(file)
                .expectedErrorType(errorType::equals)
                .expectedErrorFilePos(filePos::equals)
                .build();
    }

    @Builder(builderClassName = "Builder")
    private static class ErrorTestData {
        String file;

        // matchers
        Predicate<ErrorType> expectedErrorType;
        Predicate<FilePos> expectedErrorFilePos;
    }
}