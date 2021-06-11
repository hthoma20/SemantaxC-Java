package com.symantax.ast.node.literal.type;

import com.symantax.ast.node.list.NameTypePairList;
import com.symantax.ast.visitor.ASTVisitor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Instances of this class represent record types, such as
 * <code>
 *     record(x: int, y: string)
 * </code>
 */
@Builder
public class RecordTypeLit extends TypeLit {
    @Getter
    private NameTypePairList nameTypePairs;

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
