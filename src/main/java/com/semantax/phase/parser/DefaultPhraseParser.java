package com.semantax.phase.parser;

import com.semantax.ast.node.AstNode;
import com.semantax.ast.node.PatternDefinition;
import com.semantax.ast.node.Phrase;
import com.semantax.phase.SymbolTable;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class DefaultPhraseParser implements PhraseParser {

    @Inject
    public DefaultPhraseParser() {}

    @Override
    public Optional<AstNode> parse(Phrase phrase,
                                   List<PatternDefinition> patterns,
                                   SymbolTable symbolTable) {
        return Optional.empty();
    }
}
