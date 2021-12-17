package com.semantax.ast.visitor;

import com.semantax.ast.node.*;
import com.semantax.ast.node.list.AstNodeList;
import com.semantax.ast.node.literal.*;
import com.semantax.ast.node.literal.type.*;
import com.semantax.ast.node.pattern.PatternDefinition;
import com.semantax.ast.node.pattern.PatternInvocation;
import com.semantax.ast.node.progcall.BindProgCall;
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
import lombok.Builder;

import java.io.PrintStream;

@Builder
public class AstPrintingVisitor extends TraversalVisitor<Void> {

    @Builder.Default
    private int depth = 0;
    @Builder.Default
    private final String indent = "   ";

    @Builder.Default
    private final PrintStream output = System.out;

    private void indent() {
        for (int i = 0; i < depth; i++) {
            output.print(indent);
        }
    }

    @Override
    public Void visit(AstNode node) {
        return null;
    }

    @Override
    public Void visit(Expression exp) {
        return null;
    }

    @Override
    public Void visit(IntLit intLit) {
        indent();
        output.printf("IntLit %d (%s)%n", intLit.getValue(), intLit.getFilePos());
        return null;
    }

    @Override
    public Void visit(BoolLit boolLit) {
        indent();
        output.printf("BoolLit %b (%s)%n", boolLit.getValue(), boolLit.getFilePos());
        return null;
    }

    @Override
    public Void visit(StringLit stringLit) {
        indent();
        output.printf("StringLit %s (%s)%n", stringLit.getValue(), stringLit.getFilePos());
        return null;
    }

    @Override
    public Void visit(Module module) {
        indent();
        String modifierString = module.getModifier() == null ? "" : module.getModifier() + " ";
        String usesString = module.getModulesUsed().isEmpty() ? "" : String.format("uses %s ", module.getModulesUsed());
        output.printf("%smodule %s %s{ (%s)%n", modifierString, module.getName(), usesString, module.getFilePos());

        depth++;
        super.visit(module);
        depth--;

        indent();
        output.println("}");
        return null;
    }

    @Override
    public Void visit(ProgCall progCall) {
        indent();

        output.printf("@%s (%s)%n", progCall.getName(), progCall.getFilePos());

        depth++;
        super.visit(progCall);
        depth--;
        return null;
    }

    @Override
    public Void visit(DynamicProgcall dynamicProgcall) {
        this.visit((ProgCall) dynamicProgcall);

        return null;
    }

    @Override
    public Void visit(DeclProgCall declProgCall) {
        this.visit((ProgCall) declProgCall);

        if (declProgCall.hasDeclName()) {
            indent();
            output.printf("name: %s%n", declProgCall.getDeclName());
        }
        if (declProgCall.hasDeclType()) {
            indent();
            output.print("type: ");
            declProgCall.getDeclType().accept(this);
        }

        return null;
    }

    @Override
    public Void visit(BindProgCall bindProgCall) {
        this.visit((ProgCall) bindProgCall);

        return null;
    }

    @Override
    public Void visit(Program program) {
        indent();
        output.println("Program {");
        depth++;
        super.visit(program);
        depth--;
        indent();
        output.println("}");
        return null;
    }

    @Override
    public Void visit(Statement statement) {
        indent();

        output.printf("Statement { (%s)%n", statement.getFilePos());
        depth++;
        super.visit(statement);
        depth--;
        indent();
        output.println("}");

        return null;
    }

    @Override
    public Void visit(Word word) {
        indent();
        output.printf("Word: %s (%s)%n", word.getValue(), word.getFilePos());

        return null;
    }

    @Override
    public <T extends AstNode> Void visit(AstNodeList<T> nodes) {
        indent();

        if (nodes.size() == 0) {
            output.printf("%s [] (%s)%n", nodes.getClass().getSimpleName(), nodes.getFilePos());
            return null;
        }

        output.printf("%s [ (%s)%n", nodes.getClass().getSimpleName(), nodes.getFilePos());

        depth++;
        super.visit(nodes);
        depth--;

        indent();
        output.println("]");

        return null;
    }

    @Override
    public Void visit(TypeTypeLit typeTypeLiteral) {
        indent();
        output.printf("type (%s)%n", typeTypeLiteral.getFilePos());
        return null;
    }

    @Override
    public Void visit(IntTypeLit intTypeLiteral) {
        indent();
        output.printf("int (%s)%n", intTypeLiteral.getFilePos());
        return null;
    }

    @Override
    public Void visit(BoolTypeLit boolTypeLiteral) {
        indent();
        output.printf("bool (%s)%n", boolTypeLiteral.getFilePos());
        return null;
    }

    @Override
    public Void visit(StringTypeLit stringTypeLiteral) {
        indent();
        output.printf("string (%s)%n", stringTypeLiteral.getFilePos());
        return null;
    }

    @Override
    public Void visit(ArrayTypeLit arrayTypeLit) {
        indent();
        output.printf("array[ (%s)%n", arrayTypeLit.getFilePos());

        depth++;
        super.visit(arrayTypeLit);
        depth--;
        indent();
        output.println("]");

        return null;
    }

    @Override
    public Void visit(RecordTypeLit recordTypeLit) {
        indent();
        output.printf("record( (%s)%n", recordTypeLit.getFilePos());

        depth++;
        super.visit(recordTypeLit);
        depth--;
        indent();
        output.println(")");

        return null;
    }

    @Override
    public Void visit(FuncTypeLit funcTypeLit) {
        indent();
        output.printf("func (%s)%n", funcTypeLit.getFilePos());
        depth++;
        super.visit(funcTypeLit);
        depth--;
        return null;
    }

    @Override
    public Void visit(NameTypeLitPair nameTypeLitPair) {
        indent();
        output.printf("%s: (%s)%n", nameTypeLitPair.getName(), nameTypeLitPair.getFilePos());
        super.visit(nameTypeLitPair);
        return null;
    }

    @Override
    public Void visit(NameParsableExpressionPair nameParsableExpressionPair) {
        indent();
        output.printf("%s: (%s)%n", nameParsableExpressionPair.getName(), nameParsableExpressionPair.getFilePos());
        super.visit(nameParsableExpressionPair);
        return null;
    }

    @Override
    public Void visit(ArrayLit arrayLit) {
        indent();
        output.printf("ArrayLit (%s) [%n", arrayLit.getFilePos());
        depth++;
        super.visit(arrayLit);
        depth--;
        indent();
        output.println("]");
        return null;
    }

    @Override
    public Void visit(RecordLit recordLit) {
        indent();
        output.printf("RecordLit ( (%s)%n", recordLit.getFilePos());

        depth++;
        super.visit(recordLit);
        depth--;
        indent();
        output.println(")");

        return null;
    }

    @Override
    public Void visit(FunctionLit functionLit) {
        indent();
        output.printf("FunctionLit (%s)%n", functionLit.getFilePos());

        depth++;

        indent();
        output.println("input:");
        depth++;
        functionLit.getInput().accept(this);
        depth--;

        if (functionLit.getOutput().isPresent()) {
            indent();
            output.println("output:");
            depth++;
            functionLit.getOutput().get().accept(this);
            depth--;
        }

        if (functionLit.getReturnExpression().isPresent()) {
            indent();
            output.println("expression:");
            depth++;
            functionLit.getReturnExpression().get().accept(this);
            depth--;
        }

        if (functionLit.getStatements().isPresent()) {
            indent();
            output.println("statements:");
            depth++;
            functionLit.getStatements().get().accept(this);
            depth--;
        }

        depth--;
        indent();
        output.println(")");

        return null;
    }

    @Override
    public Void visit(Phrase phrase) {
        indent();
        output.printf("Phrase: [ (%s)%n", phrase.getFilePos());

        depth++;
        super.visit(phrase);
        depth--;

        indent();
        output.println("]");
        return null;
    }

    @Override
    public Void visit(PatternDefinition patternDefinition) {
        indent();
        output.printf("Pattern: $%s$ [ (%s)%n", patternDefinition.getSyntax(), patternDefinition.getFilePos());
        depth++;
        super.visit(patternDefinition);
        depth--;
        indent();
        output.println("]");
        return null;
    }

    @Override
    public Void visit(PatternInvocation patternInvocation) {
        indent();
        output.printf("PatternInvocation: %s (%s)%n", patternInvocation.getPatternDefinition(),
                patternInvocation.getFilePos());
        depth++;
        patternInvocation.getArguments().forEach((name, expression) -> {
            indent();
            output.printf("%s:%n", name);
            depth++;
            expression.accept(this);
            depth--;
        });
        depth--;
        return null;
    }

    @Override
    public Void visit(VariableReference variableReference) {
        indent();
        output.printf("Variable %s (%s)%n", variableReference.getDeclaration().getDeclName(),
                variableReference.getFilePos());
        return null;
    }

    @Override
    public Void visit(Type type) {
        return null;
    }

    @Override
    public Void visit(TypeType typeType) {
        output.print("type");
        return null;
    }

    @Override
    public Void visit(IntType intType) {
        output.println("int");
        return null;
    }

    @Override
    public Void visit(BoolType boolType) {
        output.print("bool");
        return null;
    }

    @Override
    public Void visit(StringType stringType) {
        output.print("string");
        return null;
    }

    @Override
    public Void visit(ArrayType arrayType) {
        output.print("array(");
        arrayType.getSubType().accept(this);
        output.print(")");
        return null;
    }

    @Override
    public Void visit(RecordType recordType) {
        output.print("record");
        return null;
    }

    @Override
    public Void visit(FuncType funcType) {
        output.print("func{");
        super.visit(funcType.getInputType());
        output.print(" -> ");
        funcType.getOutputType().ifPresent(super::visit);
        output.print("}");
        return null;
    }

    @Override
    public Void visit(VoidType voidType) {
        output.print("void");
        return null;
    }

    @Override
    public Void visit(NameTypePair nameTypePair) {
        indent();
        output.printf("%s: ", nameTypePair.getName());
        super.visit(nameTypePair);
        return null;
    }
}
