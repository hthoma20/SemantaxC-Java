package com.semantax.ast.visitor;

import com.semantax.ast.node.AstNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@Builder
public class AstToStringVisitor extends TraversalVisitor<String> {

    @Builder.Default
    private final int depth = 0;
    @Builder.Default
    private final String indent = "   ";

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
