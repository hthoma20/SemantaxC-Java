package com.semantax.phase.codegen;

import com.semantax.ast.node.Module;
import com.semantax.ast.node.Program;
import com.semantax.ast.node.Statement;
import com.semantax.ast.node.progcall.DeclProgCall;
import com.semantax.exception.CompilerException;

import javax.inject.Inject;

/**
 * Generate code for the main function
 */
public class MainCodeGenerator {

    private final ExpressionCodeGenerator expressionCodeGenerator;

    @Inject
    public MainCodeGenerator(ExpressionCodeGenerator expressionCodeGenerator) {
        this.expressionCodeGenerator = expressionCodeGenerator;
    }

    public void generateMain(CodeEmitter emitter, GeneratedTypeRegistry typeRegistry, Program program) {
        emitter.emitLine("int main(int argc, char* argv[]) {");
        emitter.indent();

        for (Statement statement : mainModule(program).getStatements()) {
            if (statement.getExpression() instanceof DeclProgCall) {
                continue;
            }
            emitter.beginLine();
            expressionCodeGenerator.generateExpression(emitter, typeRegistry, statement.getExpression());
            emitter.endLine(";");
        }

        emitter.emitLine("finalizeGarbageCollector();");
        emitter.emitLine("return 0;");
        emitter.unIndent();
        emitter.emitLine("}");
    }

    private Module mainModule(Program program) {
        return program.getModules()
                .stream()
                .filter(module -> module.getModifier() == Module.Modifier.MAIN)
                .findFirst()
                .orElseThrow(() -> CompilerException.of("No main module during code generation"));
    }
}
