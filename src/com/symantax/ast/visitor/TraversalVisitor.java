package com.symantax.ast.visitor;

import com.symantax.ast.node.*;
import com.symantax.ast.node.list.AstNodeList;
import com.symantax.ast.node.literal.*;
import com.symantax.ast.node.literal.type.*;

public class TraversalVisitor<T> implements ASTVisitor<T> {
    @Override
    public T visit(AstNode node) {
        node.accept(this);
        return null;
    }

    @Override
    public T visit(Expression exp) {
        return null;
    }

    @Override
    public T visit(IntLit intLit) {
        return null;
    }

    @Override
    public T visit(BoolLit boolLit) {
        return null;
    }

    @Override
    public T visit(StringLit stringLit) {
        return null;
    }

    @Override
    public T visit(Module module) {
        module.getSubModules().accept(this);
        module.getStatements().accept(this);
        return null;
    }

    @Override
    public T visit(ProgCall progCall) {
        progCall.getSubExpressions().accept(this);
        return null;
    }

    @Override
    public T visit(Program program) {
        program.getModules().accept(this);
        return null;
    }

    @Override
    public T visit(Statement statement) {
        statement.getExpression().accept(this);
        return null;
    }

    @Override
    public T visit(Word word) {
        return null;
    }

    @Override
    public T visit(TypeTypeLit typeTypeLiteral) {
        return null;
    }

    @Override
    public T visit(IntTypeLit intTypeLiteral) {
        return null;
    }

    @Override
    public T visit(BoolTypeLit boolTypeLiteral) {
        return null;
    }

    @Override
    public T visit(StringTypeLit stringTypeLiteral) {
        return null;
    }

    @Override
    public T visit(ArrayTypeLit arrayTypeLit) {
        return null;
    }

    @Override
    public T visit(EmptyRecord emptyRecord) {
        return null;
    }

    @Override
    public <N extends AstNode> T visit(AstNodeList<N> nodes) {
        for (N node : nodes) {
            node.accept(this);
        }
        return null;
    }
}
