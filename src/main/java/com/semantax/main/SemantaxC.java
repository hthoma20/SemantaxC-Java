package com.semantax.main;

import com.semantax.ast.node.Program;
import com.semantax.ast.visitor.AstPrintingVisitor;
import com.semantax.exception.InvalidArgumentsException;
import com.semantax.exception.ProgramErrorException;
import com.semantax.main.args.SemantaxCArgs;
import com.semantax.parser.generated.ParseException;
import com.semantax.parser.generated.SemantaxParser;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

@AllArgsConstructor
public class SemantaxC {

    private final AstPrintingVisitor printer;

    /**
     * Execute the compiler
     */
    public void execute(SemantaxCArgs args) {

        for (String inputFile : args.getInputFiles()) {
            Program program;
            try {
                program = parseProgram(inputFile);
            }
            catch (FileNotFoundException exc) {
                throw new InvalidArgumentsException(String.format("Cannot locate file: %s", inputFile));
            }
            catch (ParseException exc) {
                throw new ProgramErrorException(exc);
            }

            printer.visit(program);
        }
    }

    /**
     * @param filePath path of the file to parse, absolute or relative to the content root
     * @return a parser for the contents of the given file
     */
    private SemantaxParser getFileParser(String filePath) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(filePath);
        return new SemantaxParser(fis, StandardCharsets.UTF_8);
    }

    /**
     * Parse each input file as a program
     * @param filePath the path to the file to parse
     * @return a Program AstNode representing the file
     */
    public Program parseProgram(String filePath) throws FileNotFoundException, ParseException {
        return getFileParser(filePath).Program();
    }

}
