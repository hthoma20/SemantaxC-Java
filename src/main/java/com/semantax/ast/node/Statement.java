package com.semantax.ast.node;

import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.AstVisitor;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "Builder")
public class Statement extends AstNode {
//    @Getter
//    private Phrase phrase;
//
//    private final Eventual<Expression> expression = Eventual.unfulfilled();
    @Getter
    private final ParsableExpression parsableExpression;

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Expression getExpression() {
        return parsableExpression.getExpression();
    }

    public void setExpression(Expression expression) {
        parsableExpression.parseTo(expression);
    }

    public static class Builder {

        public Statement buildWith(FilePos filePos) {
            Statement statement = build();
            statement.setFilePos(filePos);
            return statement;
        }
    }
}
