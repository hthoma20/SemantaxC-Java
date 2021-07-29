package com.semantax.phase;

import com.semantax.logger.ErrorLogger;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class GrammarPhaseTest extends TestCase {

    private final ErrorLogger mockErrorLogger = mock(ErrorLogger.class);

    private final GrammarPhase grammarPhase = new GrammarPhase(mockErrorLogger);

    public void test_process_returnsEmptyOnError() {
        InputStream input = asInputStream("not a program");

        assertEquals(Optional.empty(), grammarPhase.process(input));
        verify(mockErrorLogger).error(any(), anyString(), anyVararg());
    }

    public void test_process_returnsProgram() {
        InputStream input = asInputStream("module foo {}");

        assertTrue(grammarPhase.process(input).isPresent());
        verifyZeroInteractions(mockErrorLogger);
    }

    private InputStream asInputStream(String string) {
        return new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
    }
}