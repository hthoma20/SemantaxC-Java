package com.symantax.ast.node.literal.type;

import com.symantax.ast.node.AstNode;
import com.symantax.ast.visitor.ASTVisitor;
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
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
