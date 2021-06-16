package com.symantax.ast.node.literal.type;

import com.symantax.ast.node.list.NameTypePairList;
import com.symantax.ast.util.FilePos;
import com.symantax.ast.visitor.ASTVisitor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

/**
 * Instances of this class represent record types, such as
 * <code>
 *     record(x: int, y: string)
 * </code>
 */
@Builder(builderClassName = "Builder")
public class RecordTypeLit extends TypeLit {
    @Getter
    private NameTypePairList nameTypePairs;

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public static class Builder {
        public RecordTypeLit buildWith(FilePos filePos) {
            RecordTypeLit recordTypeLit = build();
            recordTypeLit.setFilePos(filePos);
            return recordTypeLit;
        }
    }

}
