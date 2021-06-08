package com.symantax.ast.visitor;

import com.symantax.ast.node.*;
import com.symantax.ast.node.list.AstNodeList;
import com.symantax.ast.node.literal.*;
import com.symantax.ast.node.literal.type.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.PrintStream;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AstPrintingVisitor extends TraversalVisitor<Void> {

    @Builder.Default
    private int depth = 0;
    @Builder.Default
    private String indent = "   ";

    @Builder.Default
    private PrintStream output = System.out;

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
    public Void visit(EmptyRecord emptyRecord) {
        indent();
        output.printf("EmptyRecord (%s)%n", emptyRecord.getFilePos());
        return null;
    }
}
