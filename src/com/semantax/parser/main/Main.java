package com.semantax.parser.main;


import com.semantax.ast.node.Program;
import com.semantax.ast.visitor.AstPrintingVisitor;
import com.semantax.parser.generated.SymantaxParser;
import com.semantax.parser.generated.ParseException;


import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws ParseException, FileNotFoundException {

        FileInputStream fis = new FileInputStream("test_data/input/types.smtx");
        SymantaxParser symantaxParser = new SymantaxParser(fis);

        Program program = symantaxParser.Program();

        new AstPrintingVisitor().visit(program);
    }
}
