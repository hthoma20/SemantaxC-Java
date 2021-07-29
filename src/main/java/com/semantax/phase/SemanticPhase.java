package com.semantax.phase;

import com.semantax.ast.node.Program;
import com.semantax.logger.ErrorLogger;


import javax.inject.Inject;
import java.util.Optional;

public class SemanticPhase implements Phase<Program, Program> {

    private final ErrorLogger errorLogger;

    @Inject
    public SemanticPhase(ErrorLogger errorLogger) {
        this.errorLogger = errorLogger;
    }

    @Override
    public Optional<Program> process(Program program) {
        return Optional.of(program);
    }

}
