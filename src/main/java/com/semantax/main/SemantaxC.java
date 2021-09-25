package com.semantax.main;

import com.semantax.ast.node.Program;
import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.AstPrintingVisitor;
import com.semantax.logger.ErrorLogger;
import com.semantax.main.args.SemantaxCArgs;
import com.semantax.phase.GrammarPhase;
import com.semantax.phase.ParsePhase;

import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Optional;

public class SemantaxC {

    private final AstPrintingVisitor printer;
    private final ErrorLogger errorLogger;

    private final GrammarPhase grammarPhase;
    private final ParsePhase parsePhase;

    @Inject
    public SemantaxC(
            AstPrintingVisitor printer,
            ErrorLogger errorLogger,
            GrammarPhase grammarPhase,
            ParsePhase parsePhase) {
        this.printer = printer;
        this.errorLogger = errorLogger;
        this.grammarPhase = grammarPhase;
        this.parsePhase = parsePhase;
    }

    /**
     * Execute the compiler
     */
    public void execute(SemantaxCArgs args) {

        if (args.getInputFiles().size() != 1) {
            errorLogger.error(FilePos.none(), "Exactly 1 file is required, but %d were given",
                    args.getInputFiles().size());
            return;
        }

        String filePath = args.getInputFiles().get(0);
        Optional<InputStream> inputStream = getInputStream(filePath);

        if (!inputStream.isPresent()) {
            errorLogger.error(FilePos.none(), "Couldn't open file: %s", filePath);
            return;
        }

        Optional<Program> program = grammarPhase.process(inputStream.get());

        if (!program.isPresent()) {
            errorLogger.error(FilePos.none(), "Error parsing file: %s", filePath);
            return;
        }

        program = parsePhase.process(program.get());

        if (!program.isPresent()) {
            errorLogger.error(FilePos.none(), "Error in file: %s", filePath);
            return;
        }

        printer.visit(program.get());
    }

    /**
     * @param filePath path of the file to stream
     * @return an input stream reading the file at the given filePath
     */
    private Optional<InputStream> getInputStream(String filePath) {
        try {
            return Optional.of(new FileInputStream(filePath));
        }
        catch (FileNotFoundException e) {
            return Optional.empty();
        }
    }

}
