package com.semantax.phase;

import com.semantax.ast.node.VariableDeclaration;
import com.semantax.ast.node.progcall.DeclProgCall;
import com.semantax.exception.CompilerException;
import lombok.Builder;
import lombok.Singular;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A mapping from String to DeclProgCall that indicates
 * what symbols are currently in scope, and ties them to
 * their declarations.
 */
@Builder(builderClassName = "Builder")
public class SymbolTable {

    private final Map<String, VariableDeclaration> table = new HashMap<>();

    private final Optional<SymbolTable> parent;

    /**
     * Return the ProgCallDecl that the given name refers to.
     * Search this SymbolTable and each ancestor
     * @param name the name to lookup
     * @return an @decl with the given name
     */
    public Optional<VariableDeclaration> lookup(String name) {
        if (table.containsKey(name)) {
            return Optional.of(table.get(name));
        }

        return parent.flatMap(par -> par.lookup(name));
    }

    public void add(String name, VariableDeclaration decl) {
        if (table.containsKey(name)) {
            throw CompilerException.of("SymbolTable already contains key %s", name);
        }
        table.put(name, decl);
    }

    public boolean contains(String name) {
        return lookup(name).isPresent();
    }
}
