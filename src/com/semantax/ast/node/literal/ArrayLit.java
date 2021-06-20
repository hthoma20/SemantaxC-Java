package com.semantax.ast.node.literal;

import com.semantax.ast.node.list.ExpressionList;
import com.semantax.ast.node.literal.type.FuncTypeLit;
import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.ASTVisitor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder(builderClassName = "Builder")
public class ArrayLit extends Literal {
    @Getter
    private ExpressionList values;

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public static class Builder {

        public ArrayLit buildWith(FilePos filePos) {
            ArrayLit arrayLit = build();
            arrayLit.setFilePos(filePos);
            return arrayLit;
        }


    }

}
