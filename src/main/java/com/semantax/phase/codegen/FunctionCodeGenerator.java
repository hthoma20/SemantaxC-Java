package com.semantax.phase.codegen;

import com.semantax.ast.node.Expression;
import com.semantax.ast.node.Program;

import com.semantax.ast.node.VariableReference;
import com.semantax.ast.node.list.StatementList;
import com.semantax.ast.node.literal.FunctionLit;
import com.semantax.ast.node.literal.type.NameTypeLitPair;
import com.semantax.ast.node.progcall.DeclProgCall;
import com.semantax.ast.visitor.TraversalVisitor;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;

public class FunctionCodeGenerator {

    public static final String RETURN_LABEL = "ret";

    private final ExpressionCodeGenerator expressionCodeGenerator;
    private final StatementCodeGenerator statementCodeGenerator;

    @Inject
    public FunctionCodeGenerator(ExpressionCodeGenerator expressionCodeGenerator,
                                 StatementCodeGenerator statementCodeGenerator) {
        this.expressionCodeGenerator = expressionCodeGenerator;
        this.statementCodeGenerator = statementCodeGenerator;
    }

    public void generateFunctions(CodeEmitter emitter, GeneratedNameRegistry nameRegistry, Program program) {
        new FunctionGeneratingVisitor(emitter, nameRegistry).visit(program);
    }


    @RequiredArgsConstructor
    private class FunctionGeneratingVisitor extends TraversalVisitor<Void> {
        private final CodeEmitter emitter;
        private final GeneratedNameRegistry nameRegistry;

        private void generateClosure(FunctionLit functionLit) {
            emitter.emitLine("struct %s : Collectable {", nameRegistry.getClosureName(functionLit));
            emitter.indent();

            for (VariableReference variable : functionLit.getEnclosedVariables()) {
                emitter.emitLine("Variable* %s;", variable.getDeclaration().getDeclName());
            }

            emitter.unIndent();
            emitter.emitLine("};");
        }

        private void generateConstructor(FunctionLit function) {
            String functionName = nameRegistry.getFunctionName(function);
            String closureName = nameRegistry.getClosureName(function);
            int closureSize = function.getEnclosedVariables().size();

            emitter.emitLine("void new_%s() {", functionName);
            emitter.indent();

            if (closureSize > 0) {
                emitter.emitLine("%s* closure = (%s*) gcalloc(sizeof(%s), %d);",
                        closureName, closureName, closureName, closureSize);

                // pop arguments in reverse order
                VariableReference[] enclosedVariables =
                        function.getEnclosedVariables().toArray(new VariableReference[0]);
                for (int i = enclosedVariables.length - 1; i >= 0; i--) {
                    VariableReference enclosedVariable = enclosedVariables[i];
                    emitter.emitLine("closure->%s = (Variable*) popRoot();",
                            enclosedVariable.getDeclaration().getDeclName());
                }
                emitter.emitLine("pushRoot(closure);");
            }
            else { // no closure
                emitter.emitLine("pushRoot(nullptr);");
            }

            emitter.emitLine("new_Func(%s);", functionName);

            emitter.unIndent();
            emitter.emitLine("}");
        }

        /**
         * Generate a C function for the given function literal, the function
         * will pop an argument and a closure and will possibly push a value
         *
         * @param function the function to generate code for
         */
        private void generateFunctionDefinition(FunctionLit function) {

            emitter.emitLine("void %s() {", nameRegistry.getFunctionName(function));
            emitter.indent();

            generateFunctionHeader(function);
            emitter.emitLine("");
            generateFunctionBody(function);
            emitter.emitLine("");
            generateFunctionReturn(function);

            emitter.unIndent();
            emitter.emitLine("}");
        }

        /**
         * Generate variable declarations
         */
        private void generateFunctionHeader(FunctionLit function) {

            String argType = nameRegistry.getTypeName(function.getInput()
                    .getRepresentedType());
            String closureType = nameRegistry.getClosureName(function);

            emitter.emitLine("%s* arg = (%s*) getRoot(0);", argType, argType);
            emitter.emitLine("%s* closure = (%s*) getRoot(1);", closureType, closureType);

            for (NameTypeLitPair parameter : function.getInput().getNameTypeLitPairs()) {
                String argName = nameRegistry.getVariableName(parameter);
                emitter.emitLine("new_Variable();");
                emitter.emitLine("Variable* %s = (Variable*) getRoot(0);", argName);
                emitter.emitLine("%s->val = arg->%s;", argName, parameter.getDeclName());
            }

            for (DeclProgCall declaration : function.getLocalVariables()) {
                emitter.emitLine("new_Variable();");
                emitter.emitLine("Variable* %s = (Variable*) getRoot(0);", nameRegistry.getVariableName(declaration));
            }
        }

        private void generateFunctionBody(FunctionLit function) {
            if (function.getReturnExpression().isPresent()) {
                Expression returnExpression = function.getReturnExpression().get().getExpression();
                expressionCodeGenerator.generateExpression(emitter, nameRegistry, returnExpression);
            }
            else {
                StatementList statements = function.getStatements().get();
                statementCodeGenerator.generateStatements(emitter, nameRegistry, statements);
            }
        }


        /**
         * Generate a return label which cleans up the stack and returns
         * it is assumed that the return value will be the top of the stack,
         * if there is one
         */
        private void generateFunctionReturn(FunctionLit function) {

            emitter.emitLine("%s:", RETURN_LABEL);

            boolean hasReturnValue = function.getOutput().isPresent();

            if (hasReturnValue) {
                emitter.emitLine("Collectable* ret_val = popRoot();");
            }
            // pop the local variables, args, and closure
            int stackGrowth = function.getLocalVariables().size() +
                    function.getInput().getNameTypeLitPairs().size() + 2;
            emitter.emitLine("popRoots(%d);", stackGrowth);

            if (hasReturnValue) {
                emitter.emitLine("pushRoot(ret_val);");
            }
        }

        @Override
        public Void visit(FunctionLit functionLit) {
            super.visit(functionLit);

            generateClosure(functionLit);
            generateFunctionDefinition(functionLit);
            generateConstructor(functionLit);

            return null;
        }
    }

}
