package com.semantax.ast.node.literal.type;

import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.AstVisitor;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Builder(builderClassName = "Builder")
@Getter
public class FuncTypeLit extends TypeLit {

    @lombok.Builder.Default
    private final TypeLit inputType = RecordTypeLit.EMPTY_RECORD;
    @lombok.Builder.Default
    private final Optional<TypeLit> outputType = Optional.empty();

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public static class Builder {

        public FuncTypeLit buildWith(FilePos filePos) {
            FuncTypeLit funcTypeLit = build();
            funcTypeLit.setFilePos(filePos);
            return funcTypeLit;
        }


    }
}
