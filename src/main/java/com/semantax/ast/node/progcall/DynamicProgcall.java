package com.semantax.ast.node.progcall;

import com.semantax.ast.node.list.ParsableExpressionList;
import com.semantax.ast.type.Type;
import com.semantax.ast.visitor.AstVisitor;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

/**
 * A progcall which represents a call to a runtime function
 */
@Getter
public class DynamicProgcall extends ProgCall {
    // Types of each expected argument
    private final List<Type> parameterTypes;
    private final Optional<Type> returnType;

    // Override builder class to allow for polymorphic building
    @lombok.Builder(builderClassName = "Builder")
    DynamicProgcall(String name,
                    ParsableExpressionList subExpressions,
                    List<Type> parameterTypes,
                    Optional<Type> returnType) {
        super(name, subExpressions);
        this.parameterTypes = parameterTypes;
        this.returnType = returnType;
    }
    public static class Builder extends ProgCall.Builder {}


    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
