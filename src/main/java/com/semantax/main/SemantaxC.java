package com.semantax.main;

import com.semantax.ast.node.Program;
import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.AstPrintingVisitor;
import com.semantax.error.ErrorType;
import com.semantax.logger.ErrorLogger;
import com.semantax.main.args.SemantaxCArgs;
import com.semantax.phase.CodeGenPhase;
import com.semantax.phase.GrammarPhase;
import com.semantax.phase.ParsePhase;
import com.semantax.phase.SemanticPhase;

import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Set;

public class SemantaxC {

    private final AstPrintingVisitor printer;
    private final ErrorLogger errorLogger;

    private final GrammarPhase grammarPhase;
    private final ParsePhase parsePhase;
    private final SemanticPhase semanticPhase;
    private final CodeGenPhase codeGenPhase;

    @Inject
    public SemantaxC(
            AstPrintingVisitor printer,
            ErrorLogger errorLogger,
            GrammarPhase grammarPhase,
            ParsePhase parsePhase,
            SemanticPhase semanticPhase,
            CodeGenPhase codeGenPhase) {
        this.printer = printer;
        this.errorLogger = errorLogger;
        this.grammarPhase = grammarPhase;
        this.parsePhase = parsePhase;
        this.semanticPhase = semanticPhase;
        this.codeGenPhase = codeGenPhase;
    }

    /**
     * Execute the compiler
     */
    public void execute(SemantaxCArgs args) {

        if (args.getInputFiles().size() != 1) {

            if (args.getInputFiles().size() == 0) {
                errorLogger.error(ErrorType.MISSING_INPUT_FILE, FilePos.none(),
                        "Exactly 1 file is required, but none were given");
            }
            else {
                errorLogger.error(ErrorType.TOO_MANY_INPUT_FILES, FilePos.none(),
                        "Exactly 1 file is required, but %d were given", args.getInputFiles().size());
            }

            errorLogger.flush();
            return;
        }

        String filePath = args.getInputFiles().get(0);
        Optional<InputStream> inputStream = getInputStream(filePath);

        if (!inputStream.isPresent()) {
            errorLogger.error(ErrorType.INVALID_FILE, FilePos.none(), "Couldn't open file: %s", filePath);
            errorLogger.flush();
            return;
        }

        Optional<Program> program = grammarPhase.process(inputStream.get());

        if (!program.isPresent()) {
            errorLogger.flush();
            return;
        }

        program = parsePhase.process(program.get());

        if (!program.isPresent()) {
            errorLogger.flush();
            return;
        }

        program = semanticPhase.process(program.get());
        if (!program.isPresent()) {
            errorLogger.flush();
            return;
        }

        Optional<Set<String>> files = codeGenPhase.process(program.get());
        if (!files.isPresent()) {
            errorLogger.flush();
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
