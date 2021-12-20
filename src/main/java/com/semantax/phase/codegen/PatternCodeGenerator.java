package com.semantax.phase.codegen;

import com.semantax.ast.node.Expression;
import com.semantax.ast.node.Module;
import com.semantax.ast.node.Program;
import com.semantax.ast.node.Statement;
import com.semantax.ast.node.Word;
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

    private final FunctionDefinitionCodeGenerator functionCodeGenerator;

    @Inject
    public PatternCodeGenerator(FunctionDefinitionCodeGenerator functionCodeGenerator) {
        this.functionCodeGenerator = functionCodeGenerator;
    }

    public void generatePatterns(CodeEmitter emitter,
                                 GeneratedNameRegistry nameRegistry,
                                 Program program) {
        for (Module module : program.getModules()) {
            for (PatternDefinition pattern : module.getPatterns()) {

                emitter.annotateLine("// %s", getHint(pattern));
                emitter.emitLine("struct %s : Collectable {};", nameRegistry.getClosureName(pattern.getSemantics()));
                functionCodeGenerator.generateFunctionDefinition(
                        emitter, nameRegistry, pattern.getSemantics());

                emitter.emitLine("");
            }
        }
    }

    /**
     * Return a hint for a comment in the generated code
     * @param pattern the pattern to get a hint for
     * @return a String that identifies this pattern, not guaranteed to be unique
     */
    private String getHint(PatternDefinition pattern) {
        String syntax = pattern.getSyntax()
                .stream()
                .map(Word::getValue)
                .collect(Collectors.joining(" "));
        return String.format("$ %s $", syntax);
    }

}
