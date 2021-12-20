package com.semantax.phase.codegen;

import com.semantax.ast.node.Word;
import com.semantax.ast.node.literal.FunctionLit;
import com.semantax.ast.node.pattern.PatternDefinition;
import com.semantax.ast.util.FilePos;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores names for types that need to be generated
 */
public class GeneratedFunctionRegistry {

    private int functionIndex = 0;
    private final Map<FunctionLit, String> functionNames = new HashMap<>();

    /**
     * Get the name that has been registered for the given function.
     * If no name exists, register one and return it
     * @param function the FunctionLit to get a name for
     * @return the name of the function that should be used in generated code
     */
    public String getFunctionName(FunctionLit function) {
        FilePos filePos = function.getFilePos();
        return functionNames.computeIfAbsent(function, fun ->
                String.format("fun_%d_%d", fun.getFilePos().getLine(), functionIndex++));
    }

    /**
     * Get the name of the closure that has been registered for the given function.
     * If no name exists, register one and return it
     * @param function the FunctionLit to get a name for
     * @return the name of the closure that should be used in generated code
     */
    public String getClosureName(FunctionLit function) {
        return String.format("closure_%s", getFunctionName(function));
    }
}
