package com.semantax.phase;

import com.semantax.ast.node.progcall.DeclProgCall;
import com.semantax.exception.CompilerException;
import junit.framework.TestCase;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

public class SymbolTableTest extends TestCase {

    private final DeclProgCall mockDecl = mock(DeclProgCall.class);

    private final SymbolTable symbolTable = SymbolTable.builder()
            .parent(Optional.empty())
            .build();

    public void test_add_throwsWhenAlreadyPresent() {
        String key = "foo";
        symbolTable.add(key, mockDecl);

        assertThrows(CompilerException.class, () -> {
            symbolTable.add(key, mockDecl);
        });
    }
}