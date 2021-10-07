package com.semantax.testutil;

import com.semantax.ast.factory.StatementFactory;
import com.semantax.ast.node.AstNode;
import com.semantax.ast.node.Expression;
import com.semantax.ast.node.Module;
import com.semantax.ast.node.ParsableExpression;
import com.semantax.ast.node.Phrase;
import com.semantax.ast.node.Program;
import com.semantax.ast.node.Statement;
import com.semantax.ast.node.Word;
import com.semantax.ast.node.list.AstNodeList;
import com.semantax.ast.node.list.ModuleList;
import com.semantax.ast.node.list.StatementList;
import com.semantax.ast.node.list.WordList;
import com.semantax.ast.node.literal.IntLit;
import com.semantax.ast.node.literal.NameParsableExpressionPair;
import com.semantax.ast.node.literal.type.ArrayTypeLit;
import com.semantax.ast.node.literal.type.BoolTypeLit;
import com.semantax.ast.node.literal.type.IntTypeLit;
import com.semantax.ast.node.literal.type.NameTypeLitPair;
import com.semantax.ast.node.literal.type.StringTypeLit;
import com.semantax.ast.node.literal.type.TypeLit;
import com.semantax.ast.type.ArrayType;
import com.semantax.ast.type.NameTypePair;
import com.semantax.ast.type.Type;
import com.semantax.ast.util.FilePos;
import com.semantax.exception.CompilerException;

import java.util.Arrays;

import static com.semantax.ast.type.BoolType.BOOL_TYPE;
import static com.semantax.ast.type.IntType.INT_TYPE;
import static com.semantax.ast.type.StringType.STRING_TYPE;
import static com.semantax.ast.type.TypeType.TYPE_TYPE;
import static org.mockito.Mockito.mock;

public class AstUtil {

    public static final IntTypeLit INT_TYPE_LIT = typeLit(IntTypeLit.class, INT_TYPE);
    public static final BoolTypeLit BOOL_TYPE_LIT = typeLit(BoolTypeLit.class, BOOL_TYPE);
    public static final StringTypeLit STRING_TYPE_LIT = typeLit(StringTypeLit.class, STRING_TYPE);
    public static final ArrayTypeLit ARRAY_INT_TYPE_LIT = ArrayTypeLit.builder().subType(INT_TYPE_LIT).build();

    static {
        ARRAY_INT_TYPE_LIT.setType(TYPE_TYPE);
        ARRAY_INT_TYPE_LIT.setRepresentedType(ArrayType.builder().subType(INT_TYPE).build());
    }

    public static Program programForStatements(Statement... statements) {
        ModuleList modules = new ModuleList();
        modules.add(Module.builder()
                .name("mainModule")
                .modifier(Module.Modifier.MAIN)
                .statements(asList(StatementList.class, statements))
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

    @SafeVarargs
    public static <T extends AstNode, TList extends AstNodeList<T>> TList asList(
            Class<TList> listClass, T... elements) {
        try {
            TList list = listClass.newInstance();
            Arrays.stream(elements)
                    .forEach(list::add);
            return list;

        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static WordList asList(Word... elements) {
        WordList list = new WordList();
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

    public static NameTypePair pair(String name, Type type) {
        return NameTypePair.builder()
                .name(name)
                .type(type)
                .build();
    }

    public static NameParsableExpressionPair pair(String name, ParsableExpression expression) {
        return NameParsableExpressionPair.builder()
                .name(name)
                .expression(expression)
                .build();
    }

    public static NameTypeLitPair pair(String name, TypeLit typeLit) {
        return NameTypeLitPair.builder()
                .name(name)
                .type(typeLit)
                .build();
    }


    public static <T extends TypeLit> T typeLit(Class<T> typeLitClass, Type representedType) {
        try {
            T typeLit = typeLitClass.newInstance();
            typeLit.setType(TYPE_TYPE);
            typeLit.setRepresentedType(representedType);
            return typeLit;

        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static ParsableExpression parsedTo(Expression exp) {
        ParsableExpression parsableExpression = ParsableExpression.builder()
                .phrase(mock(Phrase.class))
                .build();
        parsableExpression.parseTo(exp);
        return parsableExpression;
    }

    public static Expression withType(Expression expression, Type type) {
        expression.setType(type);
        return expression;
    }
}
