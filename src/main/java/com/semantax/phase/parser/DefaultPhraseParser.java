package com.semantax.phase.parser;

import com.semantax.ast.node.AstNode;
import com.semantax.ast.node.pattern.PatternDefinition;
import com.semantax.ast.node.pattern.PatternInvocation;
import com.semantax.ast.node.Phrase;
import com.semantax.ast.node.PhraseElement;
import com.semantax.phase.SymbolTable;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultPhraseParser implements PhraseParser {

    private final PatternUtil patternUtil;

    private List<PatternDefinition> patterns;
    private SymbolTable symbolTable;

    @Inject
    public DefaultPhraseParser(PatternUtil patternUtil) {
        this.patternUtil = patternUtil;
    }

    /**
     * Parses a Phrase. Information about current context is given by the patterns and symbolTable.
     * From these patterns and symbolTables, a context-free grammar is constructed. For example:
     * with the following patters: [
     *      $a + b$(a: int, b:int) -> int,
     *      $a < b$(a: int, b: int) -> bool,
     *      $a and b$(a: bool, b:bool) -> bool
     * ]
     * and symbol table: {x: int, y: int, b: bool},
     * conceptually, the following grammar is constructed:
     * <code>
     * int :: "x"
     * int :: "y"
     * bool :: "b"
     * int :: int "+" int
     * bool :: int "<" int
     * bool :: bool "and" bool
     * </code>
     *
     * Then, the given phrase is parsed using this grammar. The "start symbol" is the union of the left hand side of
     * each rule in the grammar; that is, this method attempts to parse the entire given Phrase into the invocation of
     * any pattern, or to match any macro. If the parse can be done, then the proper corresponding AstNode, either a
     * PatternInvocation or StatementList is returned. If the Phrase cannot be parsed with the given patterns, then
     * Optional.empty() is returned. If the parse is ambiguous, then an arbitrary parse is returned.
     *
     * Note: this method is not thread safe
     *
     * // TODO: The choice in parse should not be arbitrary. Should an ambiguous parse (or even grammar) be an error?
     *
     * @param phrase the Phrase to parse
     * @param patterns the patterns to attempt to match Phrase with, in priority order
     * @param symbolTable a symbol table used to look up words, in case they might be identifiers
     * @return an AstNode representing the parse tree, or Optional.empty() if the phrase cannot be parsed
     */
    @Override
    public Optional<AstNode> parse(Phrase phrase,
                                   List<PatternDefinition> patterns,
                                   SymbolTable symbolTable) {
        this.patterns = patterns;
        this.symbolTable = symbolTable;

        List<PhraseElement> input = new ArrayList<>(phrase.getPhrase());
        List<PhraseElement> stack = new ArrayList<>();

        return parse(input, stack);
    }

    /**
     * @param input the un-parsed potion of the phrase. The input is in ascending order
     * @param stack the parsed portion of the phrase. The top of the stack is the largest index
     * @return the parse tree if successful, otherwise Optional.empty
     */
    private Optional<AstNode> parse(List<PhraseElement> input, List<PhraseElement> stack) {

        while (true) {

            // Find different patterns that might reduce the stack
            List<PatternDefinition> reductionCandidates = reductionCandidates(stack);

            // Try reducing the stack with each potential pattern
            // Then try to continue the parse from that state
            for (PatternDefinition reductionCandidate : reductionCandidates) {
                List<PhraseElement> branchInput = new ArrayList<>(input);
                List<PhraseElement> branchStack = new ArrayList<>(stack);

                reduce(branchStack, reductionCandidate);

                Optional<AstNode> branchParse = parse(branchInput, branchStack);
                if (branchParse.isPresent()) {
                    return branchParse;
                }
            }

            // We couldn't reduce, so check if we can shift
            if (input.isEmpty()) {
                break;
            }

            shift(input, stack);
        }

        // Once out of the loop, we know that the phrase could not be reduced any further
        // nor is there any input left to shift. So now we check if the parse was successful.
        // It is successful when the only thing left of the stack is a PatternInvocation
        if (stack.size() == 1 && stack.get(0) instanceof PatternInvocation) {
            return Optional.of((PatternInvocation) stack.get(0));
        }

        return Optional.empty();
    }

    /**
     * Reduce the stack by replacing the top n elements of the stack
     * with a ParsedPhrase, where n is the size of the given pattern.
     *
     * It is expected as a pre-condition that the pattern can actually reduce the stack
     * i.e. that canReduce(pattern) returns true
     */
    private void reduce(List<PhraseElement> stack, PatternDefinition pattern) {
        List<PhraseElement> phrase = pop(stack, pattern.getSyntax().size());
        PatternInvocation parsedPhrase = patternUtil.parse(phrase, pattern);
        stack.add(parsedPhrase);
    }

    /**
     * Determine whether the given pattern could be used to reduce the stack
     * @param pattern the pattern to check
     * @return true if the pattern can reduce the stack, false otherwise
     */
    private boolean canReduce(List<PhraseElement> stack, PatternDefinition pattern) {
        if (stack.size() < pattern.getSyntax().size()) {
            return false;
        }

        return patternUtil.matches(peek(stack, pattern.getSyntax().size()), pattern);
    }

    private List<PatternDefinition> reductionCandidates(List<PhraseElement> stack) {
        return patterns.stream()
                .filter(pattern -> this.canReduce(stack, pattern))
                .collect(Collectors.toList());
    }

    /**
     * Shift one element from input to stack
     */
    private void shift(List<PhraseElement> input, List<PhraseElement> stack) {
        stack.add(input.remove(0));
    }

    /**
     * @param n number of elements on top to return
     * @return a view into the top of the stack
     */
    private List<PhraseElement> peek(List<PhraseElement> stack, int n) {
        return stack.subList(stack.size() - n, stack.size());
    }

    /**
     * Remove and return the top n elements on the stack.
     * @param n number of elements to remove
     * @return the top n elements on the stack. The elements are kept in the order that they were pushed
     */
    private List<PhraseElement> pop(List<PhraseElement> stack, int n) {
        List<PhraseElement> top = peek(stack, n);
        List<PhraseElement> pop = new ArrayList<>(top);
        top.clear();
        return pop;
    }
}
