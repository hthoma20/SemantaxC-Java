package com.semantax.phase;

import com.semantax.ast.node.progcall.DeclProgCall;
import junit.framework.TestCase;
import lombok.AllArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class SymbolTableLookupTest extends TestCase {

    private final SymbolTable mockParent = mock(SymbolTable.class);
    private final DeclProgCall mockDecl = mock(DeclProgCall.class);

    // test data populated in constructor
    private final Optional<SymbolTable> parent;
    private final Optional<DeclProgCall> parentVal;
    private final boolean hasVal;
    private final Optional<DeclProgCall> expectedVal;

    public SymbolTableLookupTest(boolean hasParent, boolean parentHasVal, boolean hasVal, boolean expected) {
        if (parentHasVal && !hasParent) {
            throw new IllegalArgumentException("parent can have no val if there is no parent");
        }

        this.parent = Optional.of(hasParent).map(hasP -> mockParent);
        this.parentVal = Optional.of(parentHasVal).map(pHasVal -> mockDecl);
        this.hasVal = hasVal;
        this.expectedVal = Optional.of(expected).map(e -> mockDecl);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                //hasParent, parentHasVal, hasVal, expected
                {false, false, false, false},
                {false, false, true, true},
                {true, false, false, false},
                {true, false, true, true},
                {true, true, false, true},
                {true, true, true, true},
        });
    }

    @Test
    public void test_lookup() {
        // given
        String key = "foo";

        when(mockParent.lookup(key)).thenReturn(parentVal);

        SymbolTable symbolTable = SymbolTable.builder()
                .parent(parent)
                .build();

        if (hasVal) {
            symbolTable.add(key, mockDecl);
        }

        // then
        Optional<DeclProgCall> val = symbolTable.lookup(key);
        boolean contains = symbolTable.contains(key);

        // expect
        assertEquals(expectedVal, val);
        assertEquals(expectedVal.isPresent(), contains);

        // interactions
        if (hasVal) {
            verifyZeroInteractions(mockParent);
        }
        else if (expectedVal.isPresent()) {
            verify(mockParent, times(2)).lookup(key);
        }
    }
}