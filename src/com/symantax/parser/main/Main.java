package com.symantax.parser.main;


import com.symantax.ast.node.AstNode;
import com.symantax.ast.node.Program;
import com.symantax.ast.visitor.ASTVisitor;
import com.symantax.ast.visitor.AstPrintingVisitor;
import com.symantax.ast.visitor.AstToStringVisitor;
import com.symantax.parser.generated.SymantaxParser;
import com.symantax.parser.generated.ParseException;


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
