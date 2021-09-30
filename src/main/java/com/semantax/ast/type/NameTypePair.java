package com.semantax.ast.type;

import com.semantax.ast.node.AstNode;
import com.semantax.ast.visitor.AstVisitor;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "Builder")
@Getter
public class NameTypePair extends AstNode {

    private final String name;
    private final Type type;

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return null;
    }
}
