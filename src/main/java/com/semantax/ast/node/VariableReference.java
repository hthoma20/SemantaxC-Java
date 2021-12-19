package com.semantax.ast.node;

import com.semantax.ast.util.FilePos;
import com.semantax.ast.util.eventual.Eventual;
import com.semantax.ast.visitor.AstVisitor;
import com.semantax.phase.codegen.VariableScope;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "Builder")
@Getter
public class VariableReference extends Expression {
    private final VariableDeclaration declaration;
    private final Eventual<VariableScope> scope = Eventual.unfulfilled();

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

    public VariableScope getScope() {
        return scope.get();
    }

    public void setScope(VariableScope scope) {
        this.scope.fulfill(scope);
    }
}
