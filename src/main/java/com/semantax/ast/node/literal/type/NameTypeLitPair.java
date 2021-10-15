package com.semantax.ast.node.literal.type;

import com.semantax.ast.node.VariableDeclaration;
import com.semantax.ast.type.Type;
import com.semantax.ast.util.eventual.Eventual;
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
public class NameTypeLitPair extends AstNode implements VariableDeclaration {

    private final String name;
    private final TypeLit type;

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getDeclName() {
        return name;
    }

    @Override
    public Type getDeclType() {
        return type.getRepresentedType();
    }
}
