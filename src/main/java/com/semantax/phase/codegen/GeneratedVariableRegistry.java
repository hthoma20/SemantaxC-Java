package com.semantax.phase.codegen;

import com.semantax.ast.node.progcall.DeclProgCall;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores names for types that need to be generated
 */
public class GeneratedVariableRegistry {

    private int declIndex = 0;
    private final Map<DeclProgCall, String> variableNames = new HashMap<>();

    /**
     * Get the name that has been registered for the given variable declaration.
     * Note that this only registers DeclProgcalls, argument and closure references
     * should be handled separately
     * If no name exists, register one and return it
     * @param declProgCall the DeclProgCall to get a name for
     * @return the name of the variable that should be used in generated code
     */
    public String getVariableName(DeclProgCall declProgCall) {
        return variableNames.computeIfAbsent(declProgCall, decl ->
                String.format("var_%s_%d", decl.getDeclName(), declIndex++));
    }

}
