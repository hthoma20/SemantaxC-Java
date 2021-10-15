package com.semantax.ast.node.pattern;

import com.semantax.ast.node.Expression;
import com.semantax.ast.node.literal.RecordLit;
import com.semantax.ast.node.pattern.PatternDefinition;
import com.semantax.ast.visitor.AstVisitor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder(builderClassName = "Builder")
@Getter
public class PatternInvocation extends Expression {
    private final PatternDefinition patternDefinition;
    @lombok.Singular
    private final Map<String, Expression> arguments;

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
