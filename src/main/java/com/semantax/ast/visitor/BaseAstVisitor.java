package com.semantax.ast.visitor;

import com.semantax.ast.node.AstNode;
import com.semantax.ast.node.Expression;
import com.semantax.ast.node.Module;
import com.semantax.ast.node.ParsableExpression;
import com.semantax.ast.node.VariableReference;
import com.semantax.ast.node.pattern.PatternDefinition;
import com.semantax.ast.node.Phrase;
import com.semantax.ast.node.Program;
import com.semantax.ast.node.Statement;
import com.semantax.ast.node.Word;
import com.semantax.ast.node.list.AstNodeList;
import com.semantax.ast.node.literal.ArrayLit;
import com.semantax.ast.node.literal.BoolLit;
import com.semantax.ast.node.literal.FunctionLit;
import com.semantax.ast.node.literal.IntLit;
import com.semantax.ast.node.literal.NameParsableExpressionPair;
import com.semantax.ast.node.literal.RecordLit;
import com.semantax.ast.node.literal.StringLit;
import com.semantax.ast.node.literal.type.ArrayTypeLit;
import com.semantax.ast.node.literal.type.BoolTypeLit;
import com.semantax.ast.node.literal.type.FuncTypeLit;
import com.semantax.ast.node.literal.type.IntTypeLit;
import com.semantax.ast.node.literal.type.NameTypeLitPair;
import com.semantax.ast.node.literal.type.RecordTypeLit;
import com.semantax.ast.node.literal.type.StringTypeLit;
import com.semantax.ast.node.literal.type.TypeTypeLit;
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
import com.semantax.exception.CompilerException;

/**
 * An AstVisitor which throws a CompilerException when its visit method is called
 * This class is intended to be extended and its desired methods overridden
 */
public class BaseAstVisitor<T> implements AstVisitor<T> {
    private final String message = "Unimplemented visit";
    
    @Override
    public T visit(AstNode node) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(Expression exp) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(IntLit intLit) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(BoolLit boolLit) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(StringLit stringLit) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(Module module) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(Program program) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(Statement statement) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(Word word) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(ProgCall progCall) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(DynamicProgcall dynamicProgcall) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(DeclProgCall progCall) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(TypeTypeLit typeTypeLiteral) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(IntTypeLit intTypeLiteral) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(BoolTypeLit boolTypeLiteral) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(StringTypeLit stringTypeLiteral) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(ArrayTypeLit arrayTypeLit) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(RecordTypeLit recordTypeLit) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(FuncTypeLit funcTypeLit) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(NameTypeLitPair nameTypeLitPair) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(NameParsableExpressionPair nameParsableExpressionPair) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(ArrayLit arrayLit) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(RecordLit recordLit) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(FunctionLit functionLit) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(Phrase phrase) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(PatternDefinition patternDefinition) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(ParsableExpression parsableExpression) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(PatternInvocation patternInvocation) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(VariableReference variableReference) {
        throw CompilerException.of(message);
    }

    @Override
    public <N extends AstNode> T visit(AstNodeList<N> nodes) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(Type type) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(TypeType typeType) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(IntType intType) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(BoolType boolType) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(StringType stringType) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(ArrayType arrayType) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(RecordType recordType) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(FuncType funcType) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(VoidType voidType) {
        throw CompilerException.of(message);
    }

    @Override
    public T visit(NameTypePair nameTypePair) {
        throw CompilerException.of(message);
    }
}
