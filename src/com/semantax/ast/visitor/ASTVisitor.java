package com.semantax.ast.visitor;

import com.semantax.ast.node.*;
import com.semantax.ast.node.list.*;
import com.semantax.ast.node.literal.*;
import com.semantax.ast.node.literal.type.*;

public interface ASTVisitor<T> {
    T visit(AstNode node);
    T visit(Expression exp);
    T visit(IntLit intLit);
    T visit(BoolLit boolLit);
    T visit(StringLit stringLit);
    T visit(Module module);
    T visit(ProgCall progCall);
    T visit(Program program);
    T visit(Statement statement);
    T visit(Word word);

    T visit(TypeTypeLit typeTypeLiteral);
    T visit(IntTypeLit intTypeLiteral);
    T visit(BoolTypeLit boolTypeLiteral);
    T visit(StringTypeLit stringTypeLiteral);
    T visit(ArrayTypeLit arrayTypeLit);
    T visit(RecordTypeLit recordTypeLit);
    T visit(FuncTypeLit funcTypeLit);

    T visit(NameTypePair nameTypePair);
    T visit(NameExpressionPair nameExpressionPair);

    T visit(ArrayLit arrayLit);
    T visit(RecordLit recordLit);
    T visit(FunctionLit functionLit);

    T visit(VoidTypeLit voidTypeLit);

    T visit(Phrase phrase);

    <N extends AstNode> T visit(AstNodeList<N> nodes);
}
