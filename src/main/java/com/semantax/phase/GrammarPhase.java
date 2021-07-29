package com.semantax.phase;

import com.semantax.ast.node.Program;
import com.semantax.ast.util.FilePos;
import com.semantax.exception.InvalidArgumentsException;
import com.semantax.exception.ProgramErrorException;
import com.semantax.logger.ErrorLogger;
import com.semantax.parser.generated.ParseException;
import com.semantax.parser.generated.SemantaxParser;

import javax.inject.Inject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class GrammarPhase implements Phase<InputStream, Program> {

    private final ErrorLogger errorLogger;

    @Inject
    public GrammarPhase(ErrorLogger errorLogger) {
        this.errorLogger = errorLogger;
    }

    @Override
    public Optional<Program> process(InputStream input) {

        SemantaxParser parser = new SemantaxParser(input, StandardCharsets.UTF_8);

        try {
            return Optional.of(parser.Program());
        }
        catch (ParseException exc) {
            errorLogger.error(FilePos.none(), "Error parsing program: %s", exc.getMessage());
            return Optional.empty();
        }
    }
}
