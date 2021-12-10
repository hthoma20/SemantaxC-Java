package com.semantax.phase.codegen;

import com.semantax.ast.node.Module;
import com.semantax.ast.node.Program;
import com.semantax.ast.node.list.StatementList;
import com.semantax.exception.CompilerException;

import javax.inject.Inject;

/**
 * Generate code for the main function
 */
public class MainCodeGenerator {

    private final StatementCodeGenerator statementCodeGenerator;

    @Inject
    public MainCodeGenerator(StatementCodeGenerator statementCodeGenerator) {
        this.statementCodeGenerator = statementCodeGenerator;
    }

    public void generateMain(CodeEmitter emitter,
                             GeneratedTypeRegistry typeRegistry,
                             GeneratedPatternRegistry patternRegistry,
                             Program program) {
        emitter.emitLine("int main(int argc, char* argv[]) {");
        emitter.indent();

        StatementList statements = mainModule(program).getStatements();
        statementCodeGenerator.generateStatements(emitter,
                typeRegistry, patternRegistry, statements);

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
