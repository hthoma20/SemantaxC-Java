package com.semantax.phase.codegen;

import javax.inject.Inject;

/**
 * Generate code for the main function
 */
public class MainCodeGenerator {

    @Inject
    public MainCodeGenerator() { }

    public void generateMain(CodeEmitter emitter) {
        emitter.emitLine("int main(int argc, char* argv[]) {");
        emitter.indent();
        emitter.emitLine("return 0;");
        emitter.unIndent();
        emitter.emitLine("}");
    }
}
