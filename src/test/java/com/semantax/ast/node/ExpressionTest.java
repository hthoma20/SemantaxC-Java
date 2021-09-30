package com.semantax.ast.node;

import com.semantax.ast.type.Type;
import com.semantax.exception.CompilerException;
import junit.framework.TestCase;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

public class ExpressionTest extends TestCase {

    public void test_type() {
        Expression expression = new Expression() {};
        Type type = mock(Type.class);

        assertFalse(expression.hasType());
        assertThrows(CompilerException.class, () -> expression.getType());

        expression.setType(type);

        assertTrue(expression.hasType());
        assertThrows(CompilerException.class, () -> expression.setType(type));
    }
}