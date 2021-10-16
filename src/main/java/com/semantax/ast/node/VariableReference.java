package com.semantax.ast.node;

import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.AstVisitor;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "Builder")
@Getter
public class VariableReference extends Expression {
    private final VariableDeclaration declaration;

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public static class Builder {
        public VariableReference buildWith(FilePos filePos) {
            VariableReference variableReference = build();
            variableReference.setFilePos(filePos);
            return variableReference;
        }
    }

}
