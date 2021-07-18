package com.semantax.ast.node.progcall;

import com.semantax.ast.node.Expression;
import com.semantax.ast.node.Statement;
import com.semantax.ast.node.list.ExpressionList;
import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.ASTVisitor;
import lombok.Builder;
import lombok.Getter;

/**
 * A generic ProgCall. May be subclassed by more specific progcalls
 */
@Builder(builderClassName = "Builder")
@Getter
public class ProgCall extends Expression {
    private final String name;
    private final ExpressionList subExpressions;

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public static class Builder {

        public ProgCall buildWith(FilePos filePos) {
            ProgCall progCall = build();
            progCall.setFilePos(filePos);
            return progCall;
        }

    }
}
