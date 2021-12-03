package com.semantax.phase;

import com.semantax.ast.node.Program;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Perform semantic checks on the given program.
 */
public class SemanticPhase implements Phase<Program, Program> {

    @Inject
    public SemanticPhase() {}

    @Override
    public Optional<Program> process(Program input) {
        return Optional.of(input);
    }
}
