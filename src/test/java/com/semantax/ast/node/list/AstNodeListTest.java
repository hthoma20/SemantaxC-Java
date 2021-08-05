package com.semantax.ast.node.list;

import com.semantax.ast.node.AstNode;
import com.semantax.ast.visitor.ASTVisitor;
import com.semantax.exception.CompilerException;
import junit.framework.TestCase;
import org.junit.Before;

import java.util.Iterator;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.spy;

public class AstNodeListTest extends TestCase {

    @Before
    public void setUp() {
        TestAstNode.nextId = 0;
    }

    public void test_replace_insertsToBeginning() {
        // given
        AstNodeList<TestAstNode> nodeList = spy(AstNodeList.class);

        TestAstNode A, B, C, D, E;

        nodeList.add(A = new TestAstNode());
        nodeList.add(B = new TestAstNode());
        nodeList.add(C = new TestAstNode());

        AstNodeList<TestAstNode> replacements = spy(AstNodeList.class);

        replacements.add(D = new TestAstNode());
        replacements.add(E = new TestAstNode());

        // when
        nodeList.replace(A, replacements);

        // then
        Iterator<TestAstNode> iterator = nodeList.iterator();

        assertEquals(D, iterator.next());
        assertEquals(E, iterator.next());
        assertEquals(B, iterator.next());
        assertEquals(C, iterator.next());
        assertFalse(iterator.hasNext());
    }

    public void test_replace_insertsToMiddle() {
        // given
        AstNodeList<TestAstNode> nodeList = spy(AstNodeList.class);

        TestAstNode A, B, C, D, E;

        nodeList.add(A = new TestAstNode());
        nodeList.add(B = new TestAstNode());
        nodeList.add(C = new TestAstNode());

        AstNodeList<TestAstNode> replacements = spy(AstNodeList.class);

        replacements.add(D = new TestAstNode());
        replacements.add(E = new TestAstNode());

        // when
        nodeList.replace(B, replacements);

        // then
        Iterator<TestAstNode> iterator = nodeList.iterator();

        assertEquals(A, iterator.next());
        assertEquals(D, iterator.next());
        assertEquals(E, iterator.next());
        assertEquals(C, iterator.next());
        assertFalse(iterator.hasNext());
    }

    public void test_replace_insertsToEnd() {
        // given
        AstNodeList<TestAstNode> nodeList = spy(AstNodeList.class);

        TestAstNode A, B, C, D, E;

        nodeList.add(A = new TestAstNode());
        nodeList.add(B = new TestAstNode());
        nodeList.add(C = new TestAstNode());

        AstNodeList<TestAstNode> replacements = spy(AstNodeList.class);

        replacements.add(D = new TestAstNode());
        replacements.add(E = new TestAstNode());

        // when
        nodeList.replace(C, replacements);

        // then
        Iterator<TestAstNode> iterator = nodeList.iterator();

        assertEquals(A, iterator.next());
        assertEquals(B, iterator.next());
        assertEquals(D, iterator.next());
        assertEquals(E, iterator.next());
        assertFalse(iterator.hasNext());
    }

    public void test_replace_throwsWhenMissing() {
        // given
        AstNodeList<TestAstNode> nodeList = spy(AstNodeList.class);

        TestAstNode A, B, C, D, E;

        nodeList.add(A = new TestAstNode());
        nodeList.add(B = new TestAstNode());
        nodeList.add(C = new TestAstNode());

        AstNodeList<TestAstNode> replacements = spy(AstNodeList.class);

        replacements.add(D = new TestAstNode());
        replacements.add(E = new TestAstNode());

        // then
        assertThrows(CompilerException.class, () -> {
            nodeList.replace(new TestAstNode(), replacements);
        });
    }

    private static class TestAstNode extends AstNode {
        private static int nextId = 0;
        private final int id = nextId++;

        @Override
        public <T> T accept(ASTVisitor<T> visitor) {
            return null;
        }

        @Override
        public String toString() {
            return String.format("TestAstNode(%d)", id);
        }
    }
}