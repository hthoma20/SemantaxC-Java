package com.semantax.ast.node;

import com.semantax.ast.node.literal.FunctionLit;
import com.semantax.ast.visitor.AstPrintingVisitor;
import com.semantax.exception.CompilerException;
import junit.framework.TestCase;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

public class ParsableExpressionTest extends TestCase {

    private static final Phrase phrase = mock(Phrase.class);
    private static final Expression expression = mock(Expression.class);
    private static final ParsableExpression.Builder builder = ParsableExpression.builder()
            .phrase(phrase);

    public void test_getPhrase() {
        ParsableExpression parsableExpression = builder.build();
        assertEquals(phrase, parsableExpression.getPhrase());
    }

    public void test_getPhrase_throwsWithExpressionPresent() {
        ParsableExpression parsableExpression = builder.build();
        parsableExpression.parseTo(expression);
        assertThrows(CompilerException.class, () -> parsableExpression.getPhrase());
    }

    public void test_getExpression() {
        ParsableExpression parsableExpression = builder.build();
        parsableExpression.parseTo(expression);
        assertEquals(expression, parsableExpression.getExpression());
    }

    public void test_getExpression_throwsWithNoExpressionPresent() {
        ParsableExpression parsableExpression = builder.build();
        assertThrows(CompilerException.class, () -> parsableExpression.getExpression());
    }

    public void testHasExpression() {
        ParsableExpression parsableExpression = builder.build();
        assertFalse(parsableExpression.hasExpression());
        parsableExpression.parseTo(expression);
        assertTrue(parsableExpression.hasExpression());
    }

    public void test_parseTo_throwsWhenCalledTwice() {
        ParsableExpression parsableExpression = builder.build();
        parsableExpression.parseTo(expression);
        assertThrows(CompilerException.class, () -> parsableExpression.parseTo(expression));
    }
}