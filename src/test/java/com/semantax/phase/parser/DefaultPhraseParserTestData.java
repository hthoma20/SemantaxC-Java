package com.semantax.phase.parser;

import com.semantax.ast.node.pattern.PatternDefinition;
import com.semantax.ast.node.list.NameTypeLitPairList;
import com.semantax.ast.node.literal.FunctionLit;
import com.semantax.ast.node.literal.type.RecordTypeLit;

import java.util.Optional;

import static com.semantax.testutil.AstUtil.ARRAY_INT_TYPE_LIT;
import static com.semantax.testutil.AstUtil.INT_TYPE_LIT;
import static com.semantax.testutil.AstUtil.STRING_TYPE_LIT;
import static com.semantax.testutil.AstUtil.asList;
import static com.semantax.testutil.AstUtil.pair;
import static com.semantax.testutil.AstUtil.word;

public class DefaultPhraseParserTestData {

    // $a + b$ (a: int, b: int) -> int
    public static final PatternDefinition INT_ADDITION_PATTERN = PatternDefinition.builder()
            .syntax(asList(word("a"), word("+"), word("b")))
            .semantics(FunctionLit.builder()
                .input(RecordTypeLit.builder().nameTypeLitPairs(asList(NameTypeLitPairList.class,
                    pair("a", INT_TYPE_LIT),
                    pair("b", INT_TYPE_LIT))).build())
                .output(Optional.of(INT_TYPE_LIT))
                .build())
            .build();

    // $length of str$ (str: string) -> int
    public static final PatternDefinition STRING_LENGTH_PATTERN = PatternDefinition.builder()
            .syntax(asList(word("length"), word("of"), word("str")))
            .semantics(FunctionLit.builder()
                    .input(RecordTypeLit.builder().nameTypeLitPairs(asList(NameTypeLitPairList.class,
                            pair("str", STRING_TYPE_LIT))).build())
                    .output(Optional.of(INT_TYPE_LIT))
                    .build())
            .build();

    // $let var = val$ (val: int)
    // $ar << x$ (x: int, ar: array(int)) -> array(int)
    public static final PatternDefinition ARRAY_APPEND_PATTERN = PatternDefinition.builder()
            .syntax(asList(word("ar"), word(">"), word(">"), word("x")))
            .semantics(FunctionLit.builder()
                    .input(RecordTypeLit.builder().nameTypeLitPairs(asList(NameTypeLitPairList.class,
                            pair("x", INT_TYPE_LIT),
                            pair("ar", ARRAY_INT_TYPE_LIT))).build())
                    .output(Optional.of(ARRAY_INT_TYPE_LIT))
                    .build())
            .build();

    // $x >> ar$ (x: int, ar: array(int)) -> array(int)
    public static final PatternDefinition ARRAY_PREPEND_PATTERN = PatternDefinition.builder()
            .syntax(asList(word("x"), word(">"), word(">"), word("ar")))
            .semantics(FunctionLit.builder()
                    .input(RecordTypeLit.builder().nameTypeLitPairs(asList(NameTypeLitPairList.class,
                            pair("x", INT_TYPE_LIT),
                            pair("ar", ARRAY_INT_TYPE_LIT))).build())
                    .output(Optional.of(ARRAY_INT_TYPE_LIT))
                    .build())
            .build();
}
