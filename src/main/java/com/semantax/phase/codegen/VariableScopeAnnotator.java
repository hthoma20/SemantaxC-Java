package com.semantax.phase.codegen;

import com.semantax.ast.node.Program;
import com.semantax.ast.node.VariableDeclaration;
import com.semantax.ast.node.VariableReference;
import com.semantax.ast.node.literal.FunctionLit;
import com.semantax.ast.node.literal.type.NameTypeLitPair;
import com.semantax.ast.node.progcall.DeclProgCall;
import com.semantax.ast.visitor.TraversalVisitor;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;

/**
 * Annotate each VariableReference with its VariableScope
 */
public class VariableScopeAnnotator {

    @Inject
    public VariableScopeAnnotator() { }

    /**
     * Annotate each FunctionLit with the local variables that it defines (including PatternDefinitions)
     *      and with each enclosed variable
     * Annotate each VariableReference with its VariableScope
     * Annotate the give Program with the global (module-level) variables it defines
     * @param program the program to traverse
     */
    public void annotateVariables(Program program) {
        VariableScopeAnnotatingVisitor visitor = new VariableScopeAnnotatingVisitor();
        visitor.visit(program);
        program.setGlobalVariables(visitor.globalVariables);
    }


    private static class VariableScopeAnnotatingVisitor extends TraversalVisitor<Void> {
        final Set<DeclProgCall> globalVariables = new HashSet<>();

        final Stack<FunctionLit> scopeStack = new Stack<>();

        private Optional<FunctionLit> getCurrentFunction() {
            if (scopeStack.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(scopeStack.peek());
        }

        @Override
        public Void visit(FunctionLit functionLit) {
            scopeStack.push(functionLit);
            super.visit(functionLit);
            scopeStack.pop();
            return null;
        }

        @Override
        public Void visit(DeclProgCall declProgCall) {
            Optional<FunctionLit> currentFunction = getCurrentFunction();
            if (!currentFunction.isPresent()) {
                globalVariables.add(declProgCall);
            }
            else {
                currentFunction.get().addLocalVariable(declProgCall);
            }
            return null;
        }

        @Override
        public Void visit(VariableReference variableReference) {
            VariableDeclaration declaration = variableReference.getDeclaration();

            if (globalVariables.contains(declaration)) {
                variableReference.setScope(VariableScope.GLOBAL);
                return null;
            }

            // if there is no current function, then the variable reference would be a global variable
            // it is not a global variable, therefore there is a current function
            // so the Optional.get should be safe
            FunctionLit currentFunction = getCurrentFunction().get();

            if (currentFunction.getLocalVariables().contains(declaration)) {
                variableReference.setScope(VariableScope.LOCAL);
                return null;
            }

            if (isArgument(variableReference, currentFunction)) {
                variableReference.setScope(VariableScope.ARGUMENT);
                return null;
            }

            // if its not a global, locale, or argument, then it must be a closure
            variableReference.setScope(VariableScope.CLOSURE);
            currentFunction.addEnclosedVariable(declaration);
            return null;
        }

        /**
         * @param variableReference the variable to check
         * @param functionLit the function whose parameters to check
         * @return whether the variableReference is refering to a variable defined in the parameter of the function
         */
        private boolean isArgument(VariableReference variableReference, FunctionLit functionLit) {
            String referenceName = variableReference.getDeclaration().getDeclName();
            return functionLit.getInput().getNameTypeLitPairs().stream()
                    .map(NameTypeLitPair::getDeclName)
                    .anyMatch(argName -> argName.equals(referenceName));
        }
    }

}
