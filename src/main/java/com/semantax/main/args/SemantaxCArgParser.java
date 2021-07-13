package com.semantax.main.args;

import com.semantax.exception.InvalidArgumentsException;

import java.util.regex.Pattern;

public class SemantaxCArgParser {

    private static Pattern SEMANTAX_FILE_PATTERN = Pattern.compile("([\\w./\\\\]*[/\\\\])?\\p{Alpha}\\w*\\.smtx");

    /**
     * Parse command line arguments into a SemantaxCArgs object.
     * Currently, just expects a list of input files
     *
     * @param args the command line arguments
     * @return the parsed args
     * @throws InvalidArgumentsException if the args are invalid
     */
    public SemantaxCArgs parse(String[] args) throws InvalidArgumentsException {

        SemantaxCArgs.Builder builder = SemantaxCArgs.builder();

        for (String arg : args) {
            if (!isSemantaxFile(arg)) {
                throw new InvalidArgumentsException(
                        String.format("Illegal argument: '%s'. Expected semantax source file ending with .smtx", arg));
            }

            builder.inputFile(arg);
        }

        return builder.build();
    }

    private boolean isSemantaxFile(String arg) {
        return SEMANTAX_FILE_PATTERN.matcher(arg).matches();
    }

}
