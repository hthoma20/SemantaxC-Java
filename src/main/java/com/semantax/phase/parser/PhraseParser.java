package com.semantax.phase.parser;

import com.semantax.ast.node.AstNode;
import com.semantax.ast.node.pattern.PatternDefinition;
import com.semantax.ast.node.Phrase;
import com.semantax.phase.SymbolTable;

import java.util.List;
import java.util.Optional;


public interface PhraseParser {
    /**
     * Parses a phrase of semantax code into an AstNode
     * This node will either be a PatternInvocation, or a StatementList,
     * depending on whether the phrase matches to a functional pattern, or a macro
     *
     * @param phrase the Phrase to parse
     * @param patterns the patterns to attempt to match Phrase with, in priority order
     * @param symbolTable a symbol table used to look up words, in case they might be identifiers
     * @return an AstNode based on how the phrase was parsed,
     *          or an empty Optional. In this case, an appropriate error message will be printed
     */
    Optional<AstNode> parse(Phrase phrase,
                            List<PatternDefinition> patterns,
                            SymbolTable symbolTable);
}
