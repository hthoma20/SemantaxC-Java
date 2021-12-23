package com.semantax.ast.node.literal.type;

import com.semantax.ast.node.list.NameTypeLitPairList;
import com.semantax.ast.type.RecordType;
import com.semantax.ast.type.TypeType;
import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.AstVisitor;
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
            .nameTypeLitPairs(new NameTypeLitPairList())
            .buildWith(new FilePos(-1, -1));
    static {
        EMPTY_RECORD.setType(TypeType.TYPE_TYPE);
        EMPTY_RECORD.setRepresentedType(RecordType.EMPTY_TYPE);
    }

    @Getter
    private NameTypeLitPairList nameTypeLitPairs;

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
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
