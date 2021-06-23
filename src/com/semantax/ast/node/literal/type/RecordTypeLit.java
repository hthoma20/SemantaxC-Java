package com.semantax.ast.node.literal.type;

import com.semantax.ast.node.list.NameTypePairList;
import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.ASTVisitor;
import lombok.Builder;
import lombok.Getter;

/**
 * Instances of this class represent record types, such as
 * <code>
 *     record(x: int, y: string)
 * </code>
 */
@Builder(builderClassName = "Builder")
public class RecordTypeLit extends TypeLit {

    public static RecordTypeLit EMPTY_RECORD = RecordTypeLit.builder()
            .nameTypePairs(new NameTypePairList())
            .buildWith(new FilePos(-1, -1));

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
