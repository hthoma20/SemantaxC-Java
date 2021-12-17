package com.semantax.ast.node.progcall;

import com.semantax.ast.node.list.ParsableExpressionList;

/**
 * Marker class for progcalls that gives semantic meaning at compile time,
 * such as @decl, @bind, @label
 */
public class StaticProgCall extends ProgCall {
    // Override builder class to allow for polymorphic building
    @lombok.Builder(builderClassName = "Builder")
    StaticProgCall(String name, ParsableExpressionList subExpressions) {
        super(name, subExpressions);
    }
    public static class Builder extends ProgCall.Builder {}
}
