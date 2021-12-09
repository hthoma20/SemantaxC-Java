package com.semantax.ast.visitor;

import com.semantax.ast.node.*;
import com.semantax.ast.node.literal.*;
import com.semantax.ast.node.literal.type.*;
import com.semantax.ast.node.list.AstNodeList;
import com.semantax.ast.node.pattern.PatternDefinition;
import com.semantax.ast.node.pattern.PatternInvocation;
import com.semantax.ast.node.progcall.DeclProgCall;
import com.semantax.ast.node.progcall.DynamicProgcall;
import com.semantax.ast.node.progcall.ProgCall;
import com.semantax.ast.type.ArrayType;
import com.semantax.ast.type.BoolType;
import com.semantax.ast.type.FuncType;
import com.semantax.ast.type.IntType;
import com.semantax.ast.type.NameTypePair;
import com.semantax.ast.type.RecordType;
import com.semantax.ast.type.StringType;
import com.semantax.ast.type.Type;
import com.semantax.ast.type.TypeType;
import com.semantax.ast.type.VoidType;

public interface AstVisitor<T> {
    T visit(AstNode node);
    T visit(Expression exp);
    T visit(IntLit intLit);
    T visit(BoolLit boolLit);
    T visit(StringLit stringLit);
    T visit(Module module);
    T visit(Program program);
    T visit(Statement statement);
    T visit(Word word);

    T visit(ProgCall progCall);
    T visit(DeclProgCall declProgCall);
    T visit(DynamicProgcall dynamicProgcall);

    T visit(TypeTypeLit typeTypeLiteral);
    T visit(IntTypeLit intTypeLiteral);
    T visit(BoolTypeLit boolTypeLiteral);
    T visit(StringTypeLit stringTypeLiteral);
    T visit(ArrayTypeLit arrayTypeLit);
    T visit(RecordTypeLit recordTypeLit);
    T visit(FuncTypeLit funcTypeLit);

    T visit(NameTypeLitPair nameTypeLitPair);
    T visit(NameParsableExpressionPair nameParsableExpressionPair);

    T visit(ArrayLit arrayLit);
    T visit(RecordLit recordLit);
    T visit(FunctionLit functionLit);

    T visit(Phrase phrase);
    T visit(PatternDefinition patternDefinition);
    T visit(ParsableExpression parsableExpression);

    T visit(PatternInvocation patternInvocation);
    T visit(VariableReference variableReference);

    <N extends AstNode> T visit(AstNodeList<N> nodes);

    T visit(Type type);
    T visit(TypeType typeType);
    T visit(IntType intType);
    T visit(BoolType boolType);
    T visit(StringType stringType);
    T visit(ArrayType arrayType);
    T visit(RecordType recordType);
    T visit(FuncType funcType);
    T visit(VoidType voidType);
    T visit(NameTypePair nameTypePair);
}
