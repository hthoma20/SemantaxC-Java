package com.symantax.ast.visitor;

import com.symantax.ast.node.AstNode;
import com.symantax.ast.node.Program;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AstToStringVisitor extends TraversalVisitor<String> {

    @Builder.Default
    private int depth = 0;
    @Builder.Default
    private String indent = "   ";

    @Override
    public String visit(AstNode node) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream((outputStream));

        AstPrintingVisitor printingVisitor = AstPrintingVisitor.builder()
                .depth(depth)
                .indent(indent)
                .output(printStream)
                .build();

        node.accept(printingVisitor);

        String output = outputStream.toString();
        printStream.close();

        return output;
    }
}
