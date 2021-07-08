package com.semantax.ast.node.literal;

import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.ASTVisitor;
import com.semantax.ast.node.AstNode;
import com.semantax.ast.node.Expression;
import lombok.Builder;
import lombok.Getter;

/**
 * A name and a type, such as `size: int` or `student: Student`.
 * Used for the members of records, and parameters for methods and type constructors
 */
@Builder(builderClassName = "Builder")
@Getter
public class NameExpressionPair extends AstNode {

    private String name;
    private Expression expression;

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public static class Builder {

        public NameExpressionPair buildWith(FilePos filePos) {
            NameExpressionPair nameExpressionPair = build();
            nameExpressionPair.setFilePos(filePos);
            return nameExpressionPair;
        }

    }
}
