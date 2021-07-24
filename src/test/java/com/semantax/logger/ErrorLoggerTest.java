package com.semantax.logger;

import com.semantax.ast.util.FilePos;
import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static com.semantax.testutil.AssertionUtil.assertLessThan;

public class ErrorLoggerTest extends TestCase {

    public void test_errorsEmmittedInOrder() {

        ByteArrayOutputStream capture = new ByteArrayOutputStream();

        ErrorLogger errorLogger = new ErrorLogger(new PrintStream(capture));

        // log in reverse order of how they should be emitted
        FilePos p1 = new FilePos(1,1);
        FilePos p2 = new FilePos(2,1);
        errorLogger.error("e2", p2);
        errorLogger.error("e1", p1);

        // assert nothing is yet printed
        assertEquals("", capture.toString());

        errorLogger.flush();

        String output = capture.toString();

        // assert that each element is in the correct order
        int e1Index = output.indexOf("e1");
        int p1Index = output.indexOf(p1.toString());
        int e2Index = output.indexOf("e2");
        int p2Index = output.indexOf(p2.toString());

        // assert all parts were emitted
        for (int part : new int[]{e1Index, p1Index, e2Index, p2Index}) {
            assertNotSame(-1, part);
        }

        //assert expected order
        assertLessThan(e1Index, e2Index);
        assertLessThan(e1Index, p2Index);
        assertLessThan(p1Index, e2Index);
        assertLessThan(p1Index, p2Index);
    }
}