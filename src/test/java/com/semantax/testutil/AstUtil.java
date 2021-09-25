package com.semantax.testutil;

import com.semantax.ast.factory.StatementFactory;
import com.semantax.ast.node.Module;
import com.semantax.ast.node.Phrase;
import com.semantax.ast.node.Program;
import com.semantax.ast.node.Statement;
import com.semantax.ast.node.Word;
import com.semantax.ast.node.list.ModuleList;
import com.semantax.ast.node.list.StatementList;
import com.semantax.ast.node.literal.IntLit;
import com.semantax.ast.util.FilePos;

import java.util.Arrays;

public class AstUtil {

    public static Program programForStatements(Statement... statements) {
        ModuleList modules = new ModuleList();
        modules.add(Module.builder()
                .name("mainModule")
                .modifier(Module.Modifier.MAIN)
                .statements(asList(statements))
                .build());
        return new Program(modules);
    }

    public static StatementList statementsForPhrases(Phrase... phrases) {
        StatementList statements = new StatementList();
        Arrays.stream(phrases)
                .map(StatementFactory::fromPhrase)
                .forEach(statements::add);
        return statements;
    }

    public static StatementList asList(Statement... elements) {
        StatementList list = new StatementList();
        Arrays.stream(elements)
                .forEach(list::add);
        return list;
    }

    public static Word word(String text) {
        return new Word(FilePos.none(), text);
    }

    public static IntLit intLit(int val) {
        IntLit intLit = new IntLit(val);
        intLit.setFilePos(FilePos.none());
        return intLit;
    }

}
