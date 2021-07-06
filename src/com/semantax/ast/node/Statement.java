package com.semantax.ast.node;

import com.semantax.ast.node.list.StatementList;
import com.semantax.ast.node.literal.FunctionLit;
import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.ASTVisitor;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Builder(builderClassName = "Builder")
public class Statement extends AstNode {
    @Getter
    private Phrase phrase;

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public static class Builder {

        public Statement buildWith(FilePos filePos) {
            Statement statement = build();
            statement.setFilePos(filePos);
            return statement;
        }

    }
}
