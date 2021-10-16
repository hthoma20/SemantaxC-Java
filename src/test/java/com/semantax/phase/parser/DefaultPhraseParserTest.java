package com.semantax.phase.parser;

import com.semantax.ast.node.AstNode;
import com.semantax.ast.node.list.ParsableExpressionList;
import com.semantax.ast.node.literal.ArrayLit;
import com.semantax.ast.node.pattern.PatternDefinition;
import com.semantax.ast.node.Phrase;
import com.semantax.ast.node.literal.IntLit;
import com.semantax.ast.node.literal.StringLit;
import com.semantax.ast.type.ArrayType;
import com.semantax.phase.SymbolTable;
import com.semantax.phase.annotator.DefaultTypeAssignabilityChecker;
import com.semantax.phase.annotator.TypeAssignabilityChecker;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.semantax.ast.type.IntType.INT_TYPE;
import static com.semantax.ast.type.StringType.STRING_TYPE;
import static com.semantax.phase.parser.DefaultPhraseParserTestData.ARRAY_APPEND_PATTERN;
import static com.semantax.phase.parser.DefaultPhraseParserTestData.ARRAY_PREPEND_PATTERN;
import static com.semantax.phase.parser.DefaultPhraseParserTestData.INT_ADDITION_PATTERN;
import static com.semantax.phase.parser.DefaultPhraseParserTestData.STRING_LENGTH_PATTERN;
import static com.semantax.testutil.AstUtil.parsedTo;
import static com.semantax.testutil.AstUtil.withType;
import static com.semantax.testutil.AstUtil.word;

public class DefaultPhraseParserTest extends TestCase {
    private final TypeAssignabilityChecker typeAssignabilityChecker = new DefaultTypeAssignabilityChecker();
    private final PatternUtil patternUtil = new PatternUtil(typeAssignabilityChecker);

    private final DefaultPhraseParser phraseParser = new DefaultPhraseParser(patternUtil);

    public void test_twoPatternsWithNoBacktracking() {
        // given
        Phrase phrase = Phrase.builder()
                .element(parsedTo(withType(new IntLit(1), INT_TYPE)))
                .element(word("+"))
                .element(word("length"))
                .element(word("of"))
                .element(parsedTo(withType(new StringLit("testString"), STRING_TYPE)))
                .build();

        List<PatternDefinition> patterns = Arrays.asList(INT_ADDITION_PATTERN, STRING_LENGTH_PATTERN);
        SymbolTable symbolTable = SymbolTable.builder()
                .parent(Optional.empty())
                .build();

        Optional<AstNode> result = phraseParser.parse(phrase, patterns, symbolTable);

        assertTrue(result.isPresent());
    }

    public void test_twoPatternsWithBacktrackingFromLeft() {
        // given
        Phrase phrase = Phrase.builder()
                .element(parsedTo(withType(ArrayLit.builder()
                    .values(new ParsableExpressionList()).build(),
                    ArrayType.builder().subType(INT_TYPE).build())))
                .element(word(">"))
                .element(word(">"))
                .element(parsedTo(withType(new IntLit(1), INT_TYPE)))
                .element(word("+"))
                .element(parsedTo(withType(new IntLit(2), INT_TYPE)))
                .build();

        List<PatternDefinition> patterns = Arrays.asList(INT_ADDITION_PATTERN, ARRAY_APPEND_PATTERN);
        SymbolTable symbolTable = SymbolTable.builder()
                .parent(Optional.empty())
                .build();

        Optional<AstNode> result = phraseParser.parse(phrase, patterns, symbolTable);

        assertTrue(result.isPresent());
    }

    public void test_twoPatternsWithBacktrackingFromRight() {
        // given
        Phrase phrase = Phrase.builder()
                .element(parsedTo(withType(new IntLit(1), INT_TYPE)))
                .element(word("+"))
                .element(parsedTo(withType(new IntLit(2), INT_TYPE)))
                .element(word(">"))
                .element(word(">"))
                .element(parsedTo(withType(ArrayLit.builder()
                                .values(new ParsableExpressionList()).build(),
                        ArrayType.builder().subType(INT_TYPE).build())))
                .build();

        List<PatternDefinition> patterns = Arrays.asList(INT_ADDITION_PATTERN, ARRAY_PREPEND_PATTERN);
        SymbolTable symbolTable = SymbolTable.builder()
                .parent(Optional.empty())
                .build();

        Optional<AstNode> result = phraseParser.parse(phrase, patterns, symbolTable);

        assertTrue(result.isPresent());
    }

}