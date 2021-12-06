package com.semantax.phase.codegen;

import com.semantax.ast.node.Program;
import com.semantax.ast.node.literal.FunctionLit;
import com.semantax.ast.node.literal.RecordLit;
import com.semantax.ast.visitor.TraversalVisitor;

import javax.inject.Inject;

public class GeneratedTypeAggregator extends TraversalVisitor<Void> {

    @Inject
    public GeneratedTypeAggregator() { };

    /**
     * Traverse the given program and determine all types that need to be generated
     * return a registry of these types
     *
     * @param program the program to aggregate types for
     * @return a registry of all the types that need to be generated
     *        for the given program
     */
    public GeneratedTypeRegistry aggregateTypeNames(Program program) {
        GeneratedTypeRegistry registry = new GeneratedTypeRegistry();
        program.accept(new TypeAggregatorVisitor(registry));
        return registry;
    }

    private static class TypeAggregatorVisitor extends TraversalVisitor<Void> {
        private final GeneratedTypeRegistry registry;

        public TypeAggregatorVisitor(GeneratedTypeRegistry registry) {
            this.registry = registry;
        }

        @Override
        public Void visit(RecordLit recordLit) {
            recordLit.getType().accept(registry);
            return null;
        }

        @Override
        public Void visit(FunctionLit functionLit) {
            functionLit.getInput().getRepresentedType().accept(registry);
            functionLit.getOutput().ifPresent(outputType ->
                    outputType.getRepresentedType().accept(registry));
            return null;
        }
    }

}
