package com.semantax.main.args;

import com.semantax.ast.util.FilePos;
import com.semantax.error.ErrorType;
import com.semantax.logger.ErrorLogger;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import javax.inject.Inject;
import java.util.Optional;

public class SemantaxCArgParser {

    private final ErrorLogger errorLogger;
    private final ArgumentParser argParser;

    @Inject
    public SemantaxCArgParser(ErrorLogger errorLogger) {
        this.errorLogger = errorLogger;

        argParser = ArgumentParsers.newFor("SemantaxC").build()
                .description("Compile Semantax files");

        argParser.addArgument("inputFiles")
                .nargs("+")
                .help("list of Semantax (.smtx) files to compile");

        argParser.addArgument("-c")
                .dest("outputFile")
                .setDefault("out.cpp")
                .help("cpp file to write to");

        argParser.addArgument("--ast")
                .dest("astFile")
                .help("file to write a snapshot of the AST");

        argParser.addArgument("-a", "--annotations")
                .dest("annotations")
                .action(Arguments.storeTrue())
                .help("Whether to include annotations in the generated cpp file");
    }

    /**
     * Parse command line arguments into a SemantaxCArgs object.
     * Currently, just expects a list of input files
     *
     * @param args the command line arguments
     * @return the parsed args
     */
    public Optional<SemantaxCArgs> parse(String[] args) {

        Optional<Namespace> parsedArgs = parseArgs(args);
        return parsedArgs.map(this::mapToArgs);
    }

    private Optional<Namespace> parseArgs(String[] args) {

        try {
            return Optional.of(argParser.parseArgs(args));
        }
        catch (ArgumentParserException exc) {
            errorLogger.error(ErrorType.INVALID_ARGUMENTS, FilePos.none(), exc.getMessage());
        }

        return Optional.empty();
    }

    private SemantaxCArgs mapToArgs(Namespace namespace) {
        return SemantaxCArgs.builder()
                .inputFiles(namespace.getList("inputFiles"))
                .outputFile(namespace.getString("outputFile"))
                .astFile(Optional.ofNullable(namespace.getString("astFile")))
                .enableBreadCrumbs(namespace.getBoolean("annotations"))
                .build();
    }
}
