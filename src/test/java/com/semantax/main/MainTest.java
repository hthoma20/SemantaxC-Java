package com.semantax.main;

import com.semantax.main.args.SemantaxCArgParser;
import com.semantax.main.args.SemantaxCArgs;
import junit.framework.TestCase;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class MainTest extends TestCase {

    private final SemantaxC semantaxCMock = mock(SemantaxC.class);
    private final SemantaxCArgParser argParserMock = mock(SemantaxCArgParser.class);

    private final Main main = new Main(semantaxCMock, argParserMock);

    public void test_runDelegates() {

        String[] args = {"some", "args"};
        SemantaxCArgs parsedArgs = SemantaxCArgs.builder().build();

        when(argParserMock.parse(args)).thenReturn(parsedArgs);

        main.run(args);

        verify(argParserMock).parse(args);
        verify(semantaxCMock).execute(parsedArgs);
    }
}