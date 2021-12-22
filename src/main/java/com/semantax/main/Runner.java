package com.semantax.main;

import com.semantax.logger.ErrorLogger;
import com.semantax.main.args.SemantaxCArgParser;
import com.semantax.main.args.SemantaxCArgs;

import javax.inject.Inject;
import java.util.Optional;

public class Runner {

    private final SemantaxC semantaxC;
    private final SemantaxCArgParser argParser;
    private final ErrorLogger errorLogger;

    @Inject
    public Runner(SemantaxC semantaxC, SemantaxCArgParser argParser, ErrorLogger errorLogger) {
        this.semantaxC = semantaxC;
        this.argParser = argParser;
        this.errorLogger = errorLogger;
    }

    public void run(String[] args) {

        try {
            Optional<SemantaxCArgs> parsedArgs = argParser.parse(args);
            parsedArgs.ifPresent(semantaxC::execute);
        }
        catch (Exception exc) {
            System.err.printf("Error in the compiler:%n%s%n", exc.getMessage());
            exc.printStackTrace();
        }
        finally {
            errorLogger.flush();
        }
    }

}
