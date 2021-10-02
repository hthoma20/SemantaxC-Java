package com.semantax.ast.visitor;

import com.semantax.ast.node.*;
import com.semantax.ast.node.list.AstNodeList;
import com.semantax.ast.node.literal.*;
import com.semantax.ast.node.literal.type.*;
import com.semantax.ast.node.progcall.DeclProgCall;
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

public class TraversalVisitor<T> implements AstVisitor<T> {
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
        module.subNodes().forEach(node -> node.accept(this));
        return null;
    }

    @Override
    public T visit(ProgCall progCall) {
        progCall.getSubExpressions().accept(this);
        return null;
    }

    @Override
    public T visit(DeclProgCall declProgCall) {
        declProgCall.getSubExpressions().accept(this);
        return null;
    }

    @Override
    public T visit(Program program) {
        program.getModules().accept(this);
        return null;
    }

    @Override
    public T visit(Statement statement) {
        statement.getParsableExpression().accept(this);
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
        for (NameTypeLitPair nameTypeLitPair : recordTypeLit.getNameTypeLitPairs()) {
            nameTypeLitPair.accept(this);
        }
        return null;
    }

    @Override
    public T visit(FuncTypeLit funcTypeLit) {
        funcTypeLit.getInputType().accept(this);
        funcTypeLit.getOutputType().ifPresent(outputType -> outputType.accept(this));
        return null;
    }

    @Override
    public T visit(NameTypeLitPair nameTypeLitPair) {
        nameTypeLitPair.getType().accept(this);
        return null;
    }

    @Override
    public T visit(NameParsableExpressionPair nameParsableExpressionPair) {
        nameParsableExpressionPair.getExpression().accept(this);
        return null;
    }

    @Override
    public T visit(ArrayLit arrayLit) {
        arrayLit.getValues().accept(this);
        return null;
    }

    @Override
    public T visit(RecordLit recordLit) {
        for (NameParsableExpressionPair nameParsableExpressionPair : recordLit.getNameParsableExpressionPairs()) {
            nameParsableExpressionPair.accept(this);
        }
        return null;
    }

    @Override
    public T visit(FunctionLit functionLit) {
        functionLit.getInput().accept(this);
        functionLit.getOutput().ifPresent(output -> output.accept(this));
        functionLit.getStatements().ifPresent(statements -> statements.accept(this));
        functionLit.getReturnExpression().ifPresent(expression -> expression.accept(this));
        return null;
    }

    @Override
    public <N extends AstNode> T visit(AstNodeList<N> nodes) {
        for (N node : nodes) {
            node.accept(this);
        }
        return null;
    }

    @Override
    public T visit(Phrase phrase) {
        for (PhraseElement phraseElement : phrase.getPhrase()) {
            if (phraseElement instanceof AstNode) {
                ((AstNode) phraseElement).accept(this);
            }
        }
        return null;
    }

    @Override
    public T visit(PatternDefinition patternDefinition) {
        patternDefinition.getSemantics().accept(this);
        return null;
    }

    @Override
    public T visit(ParsableExpression parsableExpression) {
        if (parsableExpression.hasExpression()) {
            parsableExpression.getExpression().accept(this);
        }
        else {
            parsableExpression.getPhrase().accept(this);
        }
        return null;
    }

    @Override
    public T visit(Type type) {
        type.accept(this);
        return null;
    }

    @Override
    public T visit(TypeType typeType) {
        return null;
    }

    @Override
    public T visit(IntType intType) {
        return null;
    }

    @Override
    public T visit(BoolType boolType) {
        return null;
    }

    @Override
    public T visit(StringType stringType) {
        return null;
    }

    @Override
    public T visit(ArrayType arrayType) {
        arrayType.getSubType().accept(this);
        return null;
    }

    @Override
    public T visit(RecordType recordType) {
        return null;
    }

    @Override
    public T visit(FuncType funcType) {
        funcType.getInputType().accept(this);
        funcType.getOutputType()
                .ifPresent(outputType -> outputType.accept(this));
        return null;
    }

    @Override
    public T visit(VoidType voidType) {
        return null;
    }

    @Override
    public T visit(NameTypePair nameTypePair) {
        nameTypePair.getType().accept(this);
        return null;
    }
}
