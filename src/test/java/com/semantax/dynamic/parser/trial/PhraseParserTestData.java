package com.semantax.dynamic.parser.trial;

import com.semantax.dynamic.model.Type;
import com.semantax.dynamic.model.pattern.Pattern;
import com.semantax.dynamic.model.pattern.PatternWord;
import com.semantax.dynamic.model.pattern.Variable;

public class PhraseParserTestData {

    public static final Type INT_TYPE = new Type("int");
    public static final Type INT_ARRAY_TYPE = new Type("[int]");
    public static final Type STRING_TYPE = new Type("string");
    public static final Type VOID_TYPE = new Type("void");

    // $a + b$ (a: int, b: int)
    public static final Pattern INT_ADDITION_PATTERN = Pattern.builder()
            .addElement(new Variable("a", INT_TYPE))
            .addElement(new PatternWord("+"))
            .addElement(new Variable("b", INT_TYPE))
            .withType(INT_TYPE)
            .build();

    // $length of str$ (str: int)
    public static final Pattern STRING_LENGTH_PATTERN = Pattern.builder()
            .addElement(new PatternWord("length"))
            .addElement(new PatternWord("of"))
            .addElement(new Variable("str", STRING_TYPE))
            .withType(INT_TYPE)
            .build();

    // $let var = val$ (val: int)
    public static final Pattern LET_INT_PATTERN = Pattern.builder()
            .addElement(new PatternWord("let"))
            .addElement(new PatternWord("var"))
            .addElement(new PatternWord("="))
            .addElement(new Variable("val", INT_TYPE))
            .withType(VOID_TYPE)
            .build();

    // $x >> ar$ (x: int, ar: [int])
    public static final Pattern ARRAY_PREPEND_PATTERN = Pattern.builder()
            .addElement(new Variable("x", INT_TYPE))
            .addElement(new PatternWord(">"))
            .addElement(new PatternWord(">"))
            .addElement(new Variable("x", INT_ARRAY_TYPE))
            .build();
}
