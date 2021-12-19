package com.semantax.phase.codegen;

import com.semantax.ast.node.Expression;
import com.semantax.ast.node.Module;
import com.semantax.ast.node.Program;
import com.semantax.ast.node.Statement;
import com.semantax.ast.node.list.StatementList;
import com.semantax.ast.node.literal.FunctionLit;
import com.semantax.ast.node.pattern.PatternDefinition;
import com.semantax.ast.node.progcall.DeclProgCall;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Generate code for the main function
 */
public class PatternCodeGenerator {

    private static final String RETURN_LABEL = "ret";

    private final ExpressionCodeGenerator expressionCodeGenerator;
    private final StatementCodeGenerator statementCodeGenerator;

    @Inject
    public PatternCodeGenerator(ExpressionCodeGenerator expressionCodeGenerator,
                                StatementCodeGenerator statementCodeGenerator) {
        this.expressionCodeGenerator = expressionCodeGenerator;
        this.statementCodeGenerator = statementCodeGenerator;
    }

    public void generatePatterns(CodeEmitter emitter,
                                 GeneratedNameRegistry nameRegistry,
                                 Program program) {
        for (Module module : program.getModules()) {
            for (PatternDefinition pattern : module.getPatterns()) {
                generatePattern(emitter, nameRegistry, pattern);
                emitter.emitLine("");
            }
        }
    }

    private void generatePattern(CodeEmitter emitter,
                                 GeneratedNameRegistry nameRegistry,
                                 PatternDefinition pattern) {

        emitter.emitLine("void %s() {", nameRegistry.getPatternName(pattern));
        emitter.indent();

        generatePatternHeader(emitter, nameRegistry, pattern);
        emitter.emitLine("");
        generatePatternBody(emitter, nameRegistry, pattern);
        emitter.emitLine("");
        generatePatternReturn(emitter, pattern);

        emitter.unIndent();
        emitter.emitLine("}");
    }

    /**
     * Generate variable declarations
     */
    private void generatePatternHeader(CodeEmitter emitter,
                                       GeneratedNameRegistry nameRegistry,
                                       PatternDefinition pattern) {

        String argType = nameRegistry.getTypeName(pattern.getSemantics().getInput()
                .getRepresentedType());
        emitter.emitLine("%s* arg = (%s*) getRoot(0);", argType, argType);

        for (DeclProgCall declaration : pattern.getSemantics().getLocalVariables()) {
            emitter.emitLine("new_Variable();");
            emitter.emitLine("Variable* %s = (Variable*) getRoot(0);", nameRegistry.getVariableName(declaration));
        }
    }

    private void generatePatternBody(CodeEmitter emitter,
                                     GeneratedNameRegistry nameRegistry,
                                     PatternDefinition pattern) {
        FunctionLit function = pattern.getSemantics();

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
    private void generatePatternReturn(CodeEmitter emitter,
                                       PatternDefinition pattern) {

        emitter.emitLine("%s:", RETURN_LABEL);

        boolean hasReturnValue = pattern.getSemantics().getOutput().isPresent();

        if (hasReturnValue) {
            emitter.emitLine("Collectable* ret_val = popRoot();");
        }
        // pop variables and the arg
        emitter.emitLine("popRoots(%d);", pattern.getSemantics().getLocalVariables().size() + 1);

        if (hasReturnValue) {
            emitter.emitLine("pushRoot(ret_val);");
        }
    }

}
