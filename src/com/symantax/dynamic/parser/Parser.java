package com.symantax.dynamic.parser;

import com.symantax.dynamic.model.parsed.ParsedPhrase;
import com.symantax.dynamic.model.phrase.Phrase;

import java.util.Optional;

public interface Parser {
    /**
     * Parse the given phrase if possible
     * @param phrase the phrase to parse
     * @return the ParsedPhrase including the parse tree,
     *          or Optional.empty() if the phrase cannot be parsed
     */
    Optional<ParsedPhrase> parse(Phrase phrase);
}
