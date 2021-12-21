package com.semantax.phase.codegen;

import com.semantax.ast.node.Program;
import com.semantax.ast.node.progcall.DeclProgCall;

import javax.inject.Inject;
import java.util.Set;

/**
 * Generate declaration for the global (module-level) variables
 */
public class GlobalVariableCodeGenerator {

    public static final String INIT_FUNCTION = "initializeGlobalVariables";

    @Inject
    public GlobalVariableCodeGenerator() { }

    public void generateGlobalVariables(CodeEmitter emitter,
                                        GeneratedNameRegistry nameRegistry,
                                        Program program) {


        Set<DeclProgCall> globalVariables = program.getGlobalVariables();

        // Emit declarations for all global variables
        for (DeclProgCall declaration : globalVariables) {
            emitter.emitLine("Variable* %s;", nameRegistry.getVariableName(declaration));
        }

        // Emit a function to initialize global variables
        emitter.emitLine("void %s() {", INIT_FUNCTION);
        emitter.indent();

        emitter.emitLine("for (int i = 0; i < %d; i++) {", globalVariables.size());
        emitter.indent();
        emitter.emitLine("new_Variable();");
        emitter.unIndent();
        emitter.emitLine("}");

        int index = 0;
        for (DeclProgCall declaration : globalVariables) {
            String variableName = nameRegistry.getVariableName(declaration);
            emitter.emitLine("%s = (Variable*) getRoot(%d);", variableName, index++);
        }

        emitter.unIndent();
        emitter.emitLine("}");
    }
}
