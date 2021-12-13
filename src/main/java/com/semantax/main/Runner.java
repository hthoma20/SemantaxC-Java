package com.semantax.main;

import com.semantax.exception.InvalidArgumentsException;
import com.semantax.logger.ErrorLogger;
import com.semantax.main.args.SemantaxCArgParser;
import com.semantax.main.args.SemantaxCArgs;

import javax.inject.Inject;

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
            SemantaxCArgs parsedArgs = argParser.parse(args);
            semantaxC.execute(parsedArgs);
        }
        catch (InvalidArgumentsException exc) {
            System.err.printf("Invalid arguments:%n%s%n", exc.getMessage());
        }
        catch (Exception exc) {
            System.err.printf("Error in the compiler:%n%s%n", exc.getMessage());
            exc.printStackTrace();
            errorLogger.flush();
        }
    }

}
