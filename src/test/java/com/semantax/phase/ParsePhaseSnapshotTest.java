package com.semantax.phase;

import com.semantax.ast.node.ParsableExpression;
import com.semantax.ast.node.Program;
import com.semantax.ast.visitor.TraversalVisitor;
import com.semantax.logger.ErrorLogger;
import com.semantax.parser.generated.ParseException;
import com.semantax.parser.generated.SemantaxParser;
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
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(Parameterized.class)
@RequiredArgsConstructor
public class ParsePhaseSnapshotTest extends TestCase {
    private static final String INPUT_DIR = "./src/test/resources/test_data/parser/input";
    private static final String SNAPSHOT_DIR = "./src/test/resources/test_data/parser/snapshots";
    private final SnapshotTestUtil snapshotTestUtil = new SnapshotTestUtil(SNAPSHOT_DIR);

    // Parameter for test, populated in constructor
    private final String testFile;

    @Test
    public void test_snapshot() throws FileNotFoundException {
        TypeAssignabilityChecker typeAssignabilityChecker = new DefaultTypeAssignabilityChecker();
        PatternUtil patternUtil = new PatternUtil(typeAssignabilityChecker);
        RecordTypeUtil recordTypeUtil = new RecordTypeUtil();
        ErrorLogger errorLogger = mock(ErrorLogger.class);

        PhraseParser phraseParser = new DefaultPhraseParser(patternUtil);
        TypeAnnotator typeAnnotator = new DefaultTypeAnnotator(typeAssignabilityChecker, recordTypeUtil, errorLogger);

        ParsePhase parsePhase = new ParsePhase(errorLogger, phraseParser, typeAnnotator);

        GrammarPhase grammarPhase = new GrammarPhase(errorLogger);


        Optional<Program> program =
                grammarPhase.process(new FileInputStream(String.format("%s/%s", INPUT_DIR, testFile)));
        assertTrue(program.isPresent());

        program = parsePhase.process(program.get());
        assertTrue(program.isPresent());

        verifyZeroInteractions(errorLogger);

        new ParseAssertingVisitor().visit(program.get());

        snapshotTestUtil.assertMatchesSnapshot(testFile, program.get());
    }

    @Parameterized.Parameters
    public static Collection<String> testFiles() {
        return Arrays.asList(
                new File(INPUT_DIR).list());
    }

    private static class ParseAssertingVisitor extends TraversalVisitor<Void> {
        @Override
        public Void visit(ParsableExpression parsableExpression) {
            assertTrue("Not parsed: " + parsableExpression, parsableExpression.hasExpression());
            return null;
        }
    }
}
