package com.semantax.phase.codegen;

import com.semantax.ast.node.VariableDeclaration;
import com.semantax.ast.node.literal.type.NameTypeLitPair;
import com.semantax.ast.node.progcall.DeclProgCall;
import com.semantax.exception.CompilerException;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores names for types that need to be generated
 */
public class GeneratedVariableRegistry {

    private int declIndex = 0;
    private final Map<VariableDeclaration, String> variableNames = new HashMap<>();

    /**
     * Get the name that has been registered for the given variable declaration.
     * Note that this only registers locals and arguments, closure references
     * should be handled separately
     * If no name exists, register one and return it
     * @param declaration the VariableDeclaration to get a name for
     * @return the name of the variable that should be used in generated code
     */
    public String getVariableName(VariableDeclaration declaration) {
        return variableNames.computeIfAbsent(declaration, decl ->
                String.format("%s_%s_%d", variablePrefix(decl), decl.getDeclName(), declIndex++));
    }

    private String variablePrefix(VariableDeclaration declaration) {
        if (declaration instanceof DeclProgCall) {
            return "var";
        }
        if (declaration instanceof NameTypeLitPair) {
            return "arg";
        }
        throw new CompilerException("Unexpected VariableDeclaration type");
    }
}
