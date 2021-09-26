package com.semantax.ast.node.progcall;

import com.semantax.ast.node.Expression;
import com.semantax.ast.node.list.ParsableExpressionList;
import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.AstVisitor;
import lombok.Builder;
import lombok.Getter;

/**
 * A generic ProgCall. May be subclassed by more specific progcalls
 */
@Builder(builderClassName = "Builder")
@Getter
public class ProgCall extends Expression {
    private final String name;
    private final ParsableExpressionList subExpressions;

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
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
