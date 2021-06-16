package com.symantax.ast.visitor;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.VoidType;
import com.symantax.ast.node.*;
import com.symantax.ast.node.list.*;
import com.symantax.ast.node.literal.*;
import com.symantax.ast.node.literal.type.*;

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

    T visit(EmptyRecord emptyRecord);
    T visit(VoidTypeLit voidTypeLit);

    <N extends AstNode> T visit(AstNodeList<N> nodes);
}
