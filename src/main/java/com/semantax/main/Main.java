package com.semantax.main;

import com.semantax.ast.visitor.AstPrintingVisitor;
import com.semantax.exception.InvalidArgumentsException;
import com.semantax.exception.ProgramErrorException;
import com.semantax.main.args.SemantaxCArgParser;
import com.semantax.main.args.SemantaxCArgs;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Main {

    private final SemantaxC semantaxC;
    private final SemantaxCArgParser argParser;

    public static void main(String[] args) {
        Main main = new Main(new SemantaxC(AstPrintingVisitor.builder()
                .output(System.out)
                .build()), new SemantaxCArgParser());
        main.run(args);
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
