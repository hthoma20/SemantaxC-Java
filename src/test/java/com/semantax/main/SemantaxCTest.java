package com.semantax.main;

import com.semantax.ast.node.Program;
import com.semantax.ast.visitor.AstPrintingVisitor;
import com.semantax.logger.ErrorLogger;
import com.semantax.main.args.SemantaxCArgs;
import com.semantax.phase.GrammarPhase;
import com.semantax.phase.ParsePhase;
import junit.framework.TestCase;

import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class SemantaxCTest extends TestCase {

    private static final String TEST_FILE = "./src/test/resources/test_data/grammar/input/arrays.smtx";

    private final AstPrintingVisitor mockPrinter = mock(AstPrintingVisitor.class);
    private final ErrorLogger mockErrorLogger = mock(ErrorLogger.class);
    private final GrammarPhase mockGrammarPhase = mock(GrammarPhase.class);
    private final ParsePhase mockParsePhase = mock(ParsePhase.class);

    private final SemantaxC semantaxC =
            new SemantaxC(mockPrinter, mockErrorLogger, mockGrammarPhase, mockParsePhase);

    public void test_execute_errorWhenTooManyFiles() {
        semantaxC.execute(SemantaxCArgs.builder()
                .inputFile("file1.smtx")
                .inputFile("file2.smtx")
                .build());

        verify(mockErrorLogger).error(any(), anyString(), anyVararg());
    }

    public void test_execute_errorWhenCannotFindFile() {

        semantaxC.execute(SemantaxCArgs.builder()
                .inputFile("notarealfile.smtx")
                .build());

        verify(mockErrorLogger).error(any(), anyString(), anyVararg());
        verifyZeroInteractions(mockGrammarPhase);
    }

    public void test_execute_errorWhenCannotParse() {

        when(mockGrammarPhase.process(any())).thenReturn(Optional.empty());

        semantaxC.execute(SemantaxCArgs.builder()
                .inputFile(TEST_FILE)
                .build());

        verify(mockGrammarPhase).process(any());
        verify(mockErrorLogger).error(any(), anyString(), anyVararg());

        verifyZeroInteractions(mockParsePhase);
    }

    public void test_execute_errorWhenSemPhaseFails() {

        Program program = mock(Program.class);
        when(mockGrammarPhase.process(any())).thenReturn(Optional.of(program));
        when(mockParsePhase.process(program)).thenReturn(Optional.empty());

        semantaxC.execute(SemantaxCArgs.builder()
                .inputFile(TEST_FILE)
                .build());

        verify(mockGrammarPhase).process(any());
        verify(mockParsePhase).process(program);
        verify(mockErrorLogger).error(any(), anyString(), anyVararg());

        verifyZeroInteractions(mockPrinter);
    }

    public void test_execute_printsWhenAllSucceed() {
        Program program = mock(Program.class);
        when(mockGrammarPhase.process(any())).thenReturn(Optional.of(program));
        when(mockParsePhase.process(program)).thenReturn(Optional.of(program));

        semantaxC.execute(SemantaxCArgs.builder()
                .inputFile(TEST_FILE)
                .build());

        verify(mockGrammarPhase).process(any());
        verify(mockParsePhase).process(program);
        verify(mockPrinter).visit(program);

        verifyZeroInteractions(mockErrorLogger);
    }
}