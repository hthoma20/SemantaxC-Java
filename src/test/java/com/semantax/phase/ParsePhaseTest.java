package com.semantax.phase;

import com.semantax.ast.node.Expression;
import com.semantax.ast.node.ParsableExpression;
import com.semantax.ast.node.Phrase;
import com.semantax.ast.node.Program;
import com.semantax.ast.node.Statement;
import com.semantax.logger.ErrorLogger;
import com.semantax.parser.generated.ParseException;
import com.semantax.parser.generated.SemantaxParser;
import com.semantax.phase.annotator.TypeAnnotator;
import com.semantax.phase.parser.PhraseParser;
import junit.framework.TestCase;

import java.util.Optional;

import static com.semantax.testutil.AstUtil.programForStatements;
import static com.semantax.testutil.ParserUtil.getStringParser;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ParsePhaseTest extends TestCase {

    private final ErrorLogger mockErrorLogger = mock(ErrorLogger.class);
    private final PhraseParser mockPhraseParser = mock(PhraseParser.class);
    private final TypeAnnotator mockTypeAnnotator = mock(TypeAnnotator.class);

    private final ParsePhase parsePhase =
            new ParsePhase(
                    mockErrorLogger,
                    mockPhraseParser,
                    mockTypeAnnotator);

    public void test_component() throws ParseException {
        // given
        SemantaxParser parser = getStringParser("(1 + 2) + 3;");
        Statement statement = parser.Statement();
        Program program = programForStatements(statement);

        ParsableExpression onePlusTwoPlusThreeParsable = statement.getParsableExpression();
        ParsableExpression onePlusTwoParsable = (ParsableExpression) onePlusTwoPlusThreeParsable.getPhrase().getPhrase().get(0);

        Phrase onePlusTwoPlusThreePhrase = onePlusTwoPlusThreeParsable.getPhrase();
        Phrase onePlusTwoPhrase = onePlusTwoParsable.getPhrase();

        Expression onePlusTwoPlusThreeParse = mock(Expression.class);
        Expression onePlusTwoParse = mock(Expression.class);

        when(mockPhraseParser.parse(eq(onePlusTwoPhrase), any(), any()))
                .thenReturn(Optional.of(onePlusTwoParse));
        when(mockPhraseParser.parse(eq(onePlusTwoPlusThreePhrase), any(), any()))
                .thenReturn(Optional.of(onePlusTwoPlusThreeParse));

        // when
        Optional<Program> processedProgram = parsePhase.process(program);

        // then
        assertTrue(processedProgram.isPresent());

        Statement parsedStatement = processedProgram.get()
                .getModules().get(0)
                .getStatements().get(0);

        assertEquals(onePlusTwoPlusThreeParse, parsedStatement.getExpression());

        // interactions
        verify(mockPhraseParser).parse(eq(onePlusTwoPhrase), any(), any());
        verify(mockPhraseParser).parse(eq(onePlusTwoPlusThreePhrase), any(), any());
        verify(mockTypeAnnotator).visit(onePlusTwoPlusThreeParsable);
        verify(mockTypeAnnotator).visit(onePlusTwoParsable);
    }
}