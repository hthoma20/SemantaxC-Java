package com.semantax.ast.node.literal;

import com.semantax.ast.node.list.StatementList;
import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.ASTVisitor;
import com.semantax.ast.node.Expression;
import com.semantax.ast.node.literal.type.RecordTypeLit;
import com.semantax.ast.node.literal.type.TypeLit;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Builder(builderClassName = "Builder")
@Getter
public class FunctionLit extends Literal {
    @lombok.Builder.Default
    private RecordTypeLit input = RecordTypeLit.EMPTY_RECORD;
    @lombok.Builder.Default
    private Optional<TypeLit> output = Optional.empty();

    // A function will either be a list of statements, or a single expression
    @lombok.Builder.Default
    private Optional<StatementList> statements = Optional.empty();
    @lombok.Builder.Default
    private Optional<Expression> returnExpression = Optional.empty();

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public static class Builder {

        public FunctionLit buildWith(FilePos filePos) {
            FunctionLit functionLit = build();
            functionLit.setFilePos(filePos);
            return functionLit;
        }

    }
}
