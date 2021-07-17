package com.semantax.main;

import com.semantax.exception.InvalidArgumentsException;
import com.semantax.exception.ProgramErrorException;
import com.semantax.main.args.SemantaxCArgParser;
import com.semantax.main.args.SemantaxCArgs;

import javax.inject.Inject;

public class Runner {

    private final SemantaxC semantaxC;
    private final SemantaxCArgParser argParser;

    @Inject
    public Runner(SemantaxC semantaxC, SemantaxCArgParser argParser) {
        this.semantaxC = semantaxC;
        this.argParser = argParser;
    }

    public void run(String[] args) {

        try {
            SemantaxCArgs parsedArgs = argParser.parse(args);
            semantaxC.execute(parsedArgs);
        }
        catch (InvalidArgumentsException exc) {
            System.err.printf("Invalid arguments:%n%s%n", exc.getMessage());
        }
        catch (ProgramErrorException exc) {
            System.err.printf("Compile error:%n%s%n", exc.getMessage());
        }
    }

}
