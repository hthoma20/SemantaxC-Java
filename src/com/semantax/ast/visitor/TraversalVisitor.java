package com.semantax.ast.visitor;

import com.semantax.ast.node.*;
import com.semantax.ast.node.list.AstNodeList;
import com.semantax.ast.node.literal.*;
import com.semantax.ast.node.literal.type.*;

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
        arrayTypeLit.getSubType().accept(this);
        return null;
    }

    @Override
    public T visit(RecordTypeLit recordTypeLit) {
        for (NameTypePair nameTypePair : recordTypeLit.getNameTypePairs()) {
            nameTypePair.accept(this);
        }
        return null;
    }

    @Override
    public T visit(FuncTypeLit funcTypeLit) {
        funcTypeLit.getInputType().accept(this);
        funcTypeLit.getOutputType().accept(this);
        return null;
    }

    @Override
    public T visit(NameTypePair nameTypePair) {
        nameTypePair.getType().accept(this);
        return null;
    }

    @Override
    public T visit(ArrayLit arrayLit) {
        arrayLit.getValues().accept(this);
        return null;
    }

    @Override
    public T visit(EmptyRecord emptyRecord) {
        return null;
    }

    @Override
    public T visit(VoidTypeLit voidTypeLit) {
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
