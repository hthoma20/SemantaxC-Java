package com.semantax.main;

import com.semantax.logger.ErrorLogger;
import com.semantax.main.args.SemantaxCArgParser;
import com.semantax.main.args.SemantaxCArgs;
import junit.framework.TestCase;

import static org.mockito.Mockito.*;

public class RunnerTest extends TestCase {

    private final SemantaxC semantaxCMock = mock(SemantaxC.class);
    private final SemantaxCArgParser argParserMock = mock(SemantaxCArgParser.class);
    private final ErrorLogger errorLoggerMock = mock(ErrorLogger.class);

    private final Runner runner = new Runner(semantaxCMock, argParserMock, errorLoggerMock);

    public void test_runDelegates() {

        String[] args = {"some", "args"};
        SemantaxCArgs parsedArgs = SemantaxCArgs.builder().build();

        when(argParserMock.parse(args)).thenReturn(parsedArgs);

        runner.run(args);

        verify(argParserMock).parse(args);
        verify(semantaxCMock).execute(parsedArgs);
    }
}