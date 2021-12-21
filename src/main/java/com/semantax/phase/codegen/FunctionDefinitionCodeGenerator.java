package com.semantax.phase.codegen;

import com.semantax.ast.node.Expression;
import com.semantax.ast.node.list.StatementList;
import com.semantax.ast.node.literal.FunctionLit;
import com.semantax.ast.node.progcall.DeclProgCall;

import javax.inject.Inject;


/**
 * Generate code for a runtime function
 * This encapsulates shared logic for patterns and functions
 */
public class FunctionDefinitionCodeGenerator {

    private static final String RETURN_LABEL = "ret";

    private final ExpressionCodeGenerator expressionCodeGenerator;
    private final StatementCodeGenerator statementCodeGenerator;

    @Inject
    public FunctionDefinitionCodeGenerator(ExpressionCodeGenerator expressionCodeGenerator,
                                           StatementCodeGenerator statementCodeGenerator) {
        this.expressionCodeGenerator = expressionCodeGenerator;
        this.statementCodeGenerator = statementCodeGenerator;
    }

    /**
     * Generate a C function for the given function literal, the function
     * will pop an argument and a closure and will possibly push a value
     *
     * @param emitter the CodeEmitter to emit to
     * @param nameRegistry the registry to look up names in
     * @param function the function to generate code for
     */
    public void generateFunctionDefinition(CodeEmitter emitter,
                                           GeneratedNameRegistry nameRegistry,
                                           FunctionLit function) {

        emitter.emitLine("void %s() {", nameRegistry.getFunctionName(function));
        emitter.indent();

        generateFunctionHeader(emitter, nameRegistry, function);
        emitter.emitLine("");
        generateFunctionBody(emitter, nameRegistry, function);
        emitter.emitLine("");
        generateFunctionReturn(emitter, function);

        emitter.unIndent();
        emitter.emitLine("}");
    }

    /**
     * Generate variable declarations
     */
    private void generateFunctionHeader(CodeEmitter emitter,
                                        GeneratedNameRegistry nameRegistry,
                                        FunctionLit function) {

        String argType = nameRegistry.getTypeName(function.getInput()
                .getRepresentedType());
        String closureType = nameRegistry.getClosureName(function);

        emitter.emitLine("%s* arg = (%s*) getRoot(0);", argType, argType);
        emitter.emitLine("%s* closure = (%s*) getRoot(1);", closureType, closureType);

        for (DeclProgCall declaration : function.getLocalVariables()) {
            emitter.emitLine("new_Variable();");
            emitter.emitLine("Variable* %s = (Variable*) getRoot(0);", nameRegistry.getVariableName(declaration));
        }
    }

    private void generateFunctionBody(CodeEmitter emitter,
                                      GeneratedNameRegistry nameRegistry,
                                      FunctionLit function) {
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
    private void generateFunctionReturn(CodeEmitter emitter,
                                        FunctionLit function) {

        emitter.emitLine("%s:", RETURN_LABEL);

        boolean hasReturnValue = function.getOutput().isPresent();

        if (hasReturnValue) {
            emitter.emitLine("Collectable* ret_val = popRoot();");
        }
        // pop the variables, arg, and closure
        int stackGrowth = function.getLocalVariables().size() + 2;
        emitter.emitLine("popRoots(%d);", stackGrowth);

        if (hasReturnValue) {
            emitter.emitLine("pushRoot(ret_val);");
        }
    }

}
