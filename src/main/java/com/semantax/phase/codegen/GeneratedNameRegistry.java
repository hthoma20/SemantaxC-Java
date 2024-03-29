package com.semantax.phase.codegen;

import com.semantax.ast.node.VariableDeclaration;
import com.semantax.ast.node.literal.FunctionLit;

import com.semantax.ast.type.Type;
import lombok.Builder;

/**
 * Facade for the various different registries for tracking
 * names in generated code for constructs in the AST
 */
@Builder(builderClassName = "Builder")
public class GeneratedNameRegistry {
    private final GeneratedTypeRegistry typeRegistry;
    private final GeneratedVariableRegistry variableRegistry;
    private final GeneratedFunctionRegistry functionRegistry;

    public String getTypeName(Type type) {
        return type.accept(typeRegistry);
    }

    public String getVariableName(VariableDeclaration declaration) {
        return variableRegistry.getVariableName(declaration);
    }

    public String getFunctionName(FunctionLit functionLit) {
        return functionRegistry.getFunctionName(functionLit);
    }

    public String getClosureName(FunctionLit functionLit) {
        return functionRegistry.getClosureName(functionLit);
    }
}
