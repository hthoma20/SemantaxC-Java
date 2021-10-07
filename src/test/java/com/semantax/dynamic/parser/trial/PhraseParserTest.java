package com.semantax.dynamic.parser.trial;

import com.semantax.dynamic.model.parsed.ParsedPhrase;
import com.semantax.dynamic.model.phrase.Expression;
import com.semantax.dynamic.model.phrase.Phrase;

import com.semantax.dynamic.model.phrase.PhraseWord;
import com.semantax.dynamic.parser.trial.PhraseParser;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static com.semantax.dynamic.parser.trial.PhraseParserTestData.*;


public class PhraseParserTest {

    @Test
    public void parseTwoPatternsWithNoBacktracking() {
        PhraseParser parser = PhraseParser.builder()
                .addPattern(INT_ADDITION_PATTERN)
                .addPattern(STRING_LENGTH_PATTERN)
                .build();

        Phrase phrase = Phrase.builder()
                .addElement(new Expression(INT_TYPE))
                .addElement(new PhraseWord("+"))
                .addElement(new PhraseWord("length"))
                .addElement(new PhraseWord("of"))
                .addElement(new Expression(STRING_TYPE))
                .build();

        Optional<ParsedPhrase> result = parser.parse(phrase);

        assertTrue("Expected to be able to parse", result.isPresent());
    }

    @Test
    public void parsePatternsWithBacktrackingFromLeft() {
        PhraseParser parser = PhraseParser.builder()
                .addPattern(LET_INT_PATTERN)
                .addPattern(INT_ADDITION_PATTERN)
                .build();

        Phrase phrase = Phrase.builder()
                .addElement(new PhraseWord("let"))
                .addElement(new PhraseWord("var"))
                .addElement(new PhraseWord("="))
                .addElement(new Expression(INT_TYPE))
                .addElement(new PhraseWord("+"))
                .addElement(new Expression(INT_TYPE))
                .build();

        Optional<ParsedPhrase> result = parser.parse(phrase);

        assertTrue("Expected to be able to parse", result.isPresent());
    }

    @Test
    public void parsePatternsWithBacktrackingFromRight() {
        PhraseParser parser = PhraseParser.builder()
                .addPattern(ARRAY_PREPEND_PATTERN)
                .addPattern(INT_ADDITION_PATTERN)
                .build();
        // int + int >> ar
        Phrase phrase = Phrase.builder()
                .addElement(new Expression(INT_TYPE))
                .addElement(new PhraseWord("+"))
                .addElement(new Expression(INT_TYPE))
                .addElement(new PhraseWord(">"))
                .addElement(new PhraseWord(">"))
                .addElement(new Expression(INT_ARRAY_TYPE))
                .build();

        Optional<ParsedPhrase> result = parser.parse(phrase);

        assertTrue("Expected to be able to parse", result.isPresent());
    }
}