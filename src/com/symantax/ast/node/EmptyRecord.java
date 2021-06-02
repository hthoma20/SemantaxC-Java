package com.symantax.ast.node;

import com.symantax.ast.visitor.ASTVisitor;
import lombok.Getter;
import lombok.ToString;

/**
 * The Empty Record is literally "()"
 * It can represent the empty record type, empty tuple type,
 * empty record literal, or empty tuple literal based on context
 */
public class EmptyRecord extends Expression {

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
