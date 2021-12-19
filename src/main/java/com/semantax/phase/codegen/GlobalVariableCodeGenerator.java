package com.semantax.phase.codegen;

import com.semantax.ast.node.Program;
import com.semantax.ast.node.progcall.DeclProgCall;

import javax.inject.Inject;

/**
 * Generate declaration for the global (module-level) variables
 */
public class GlobalVariableCodeGenerator {

    @Inject
    public GlobalVariableCodeGenerator() { }

    public void generateGlobalVariables(CodeEmitter emitter,
                                        GeneratedNameRegistry nameRegistry,
                                        Program program) {

        for (DeclProgCall declaration : program.getGlobalVariables()) {
            emitter.emitLine("Variable* %s;", nameRegistry.getVariableName(declaration));
        }
    }
}
