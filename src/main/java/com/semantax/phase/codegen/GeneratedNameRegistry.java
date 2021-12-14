package com.semantax.phase.codegen;

import com.semantax.ast.node.pattern.PatternDefinition;
import com.semantax.ast.node.progcall.DeclProgCall;
import com.semantax.ast.type.Type;
import lombok.Builder;

/**
 * Facade for the various different registries for tracking
 * names in generated code for constructs in the AST
 */
@Builder(builderClassName = "Builder")
public class GeneratedNameRegistry {
    private final GeneratedTypeRegistry typeRegistry;
    private final GeneratedPatternRegistry patternRegistry;
    private final GeneratedVariableRegistry variableRegistry;

    public String getTypeName(Type type) {
        return type.accept(typeRegistry);
    }

    public String getPatternName(PatternDefinition pattern) {
        return patternRegistry.getPatternName(pattern);
    }

    public String getVariableName(DeclProgCall declProgCall) {
        return variableRegistry.getVariableName(declProgCall);
    }
}
