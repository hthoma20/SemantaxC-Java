package com.semantax.phase.codegen;

/**
 * What type of declaration is a variable referring to
 */
public enum VariableScope {
    GLOBAL, // a reference to a module level variable
    LOCAL, // a reference to a variable declared in this scope
    ARGUMENT, // a reference to a local argument
    CLOSURE, // a reference to a variable defined in an enclosing scope
}
