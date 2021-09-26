package com.semantax.ast.node.literal.type;

import com.semantax.ast.visitor.AstVisitor;
import com.semantax.ast.node.AstNode;
import lombok.Builder;
import lombok.Getter;

/**
 * A name and a type, such as `size: int` or `student: Student`.
 * Used for the members of records, and parameters for methods and type constructors
 */
@Builder
@Getter
public class NameTypePair extends AstNode {

    private String name;
    private TypeLit type;

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
