package com.semantax.ast.visitor;

import com.semantax.ast.node.*;
import com.semantax.ast.node.list.AstNodeList;
import com.semantax.ast.node.literal.*;
import com.semantax.ast.node.literal.type.*;
import com.semantax.ast.node.progcall.DeclProgCall;
import com.semantax.ast.node.progcall.ProgCall;
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
        String usesString = module.getModulesUsed() == null ? "" : String.format("uses %s ", module.getModulesUsed());
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

        super.visit(progCall);
        return null;
    }

    @Override
    public Void visit(DeclProgCall declProgCall) {
        indent();

        output.printf("Decl @%s (%s)%n", declProgCall.getName(), declProgCall.getFilePos());
        indent();
        output.printf("name: %s%n", declProgCall.getDeclName());
        super.visit(declProgCall);
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
    public Void visit(NameTypePair nameTypePair) {
        indent();
        output.printf("%s: (%s)%n", nameTypePair.getName(), nameTypePair.getFilePos());
        super.visit(nameTypePair);
        return null;
    }

    @Override
    public Void visit(NameExpressionPair nameExpressionPair) {
        indent();
        output.printf("%s: (%s)%n", nameExpressionPair.getName(), nameExpressionPair.getFilePos());
        super.visit(nameExpressionPair);
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
    public Void visit(VoidTypeLit voidTypeLit) {
        indent();
        output.println("VoidTypeLit");
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
}
