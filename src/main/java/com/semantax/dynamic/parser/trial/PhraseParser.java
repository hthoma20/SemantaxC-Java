package com.semantax.dynamic.parser.trial;

import com.semantax.dynamic.model.parsed.ParsedPhrase;
import com.semantax.dynamic.model.pattern.Pattern;
import com.semantax.dynamic.model.phrase.Phrase;
import com.semantax.dynamic.model.phrase.PhraseElement;
import com.semantax.dynamic.parser.Parser;

import java.util.*;
import java.util.stream.Collectors;

public class PhraseParser implements Parser {

    private final List<Pattern> patterns;

    private PhraseParser(Builder builder) {
        this.patterns = builder.patterns;
    }

    @Override
    public Optional<ParsedPhrase> parse(Phrase phrase) {
        return parse(phrase.cloneElements(), new ArrayList<>());
    }

    // top of the stack is the largest index (index 0 is bottom of stack)
    // input read in-index order
    private Optional<ParsedPhrase> parse(List<PhraseElement> input, List<PhraseElement> stack) {
        while (true) {

            // Find different patterns that might reduce the stack
            List<Pattern> reductionCandidates = reductionCandidates(stack);

            // Try reducing the stack with each potential pattern
            // Then try to continue the parse from that state
            for (Pattern reductionCandidate : reductionCandidates) {
                List<PhraseElement> branchInput = new ArrayList<>(input);
                List<PhraseElement> branchStack = new ArrayList<>(stack);

                reduce(branchStack, reductionCandidate);

                Optional<ParsedPhrase> branchParse = parse(branchInput, branchStack);
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
        // It is successful when the only thing left of the stack is a ParsedPhrase (thought of as the start symbol)
        if (stack.size() == 1 && stack.get(0) instanceof ParsedPhrase) {
            return Optional.of((ParsedPhrase) stack.get(0));
        }

        return Optional.empty();
    }

    /**
     * Shift one element from input to stack
     */
    private void shift(List<PhraseElement> input, List<PhraseElement> stack) {
        stack.add(input.remove(0));
    }

    /**
     * Reduce the stack by replacing the top n elements of the stack
     * with a ParsedPhrase, where n is the size of the given pattern.
     *
     * It is expected as a pre-condition that the pattern can actually reduce the stack
     * i.e. that canReduce(pattern) returns true
     */
    private void reduce(List<PhraseElement> stack, Pattern pattern) {
        List<PhraseElement> phrase = pop(stack, pattern.size());

        Optional<ParsedPhrase> parsedPhrase = pattern.parse(phrase);

        if (!parsedPhrase.isPresent()) {
            throw new RuntimeException("Expected to be able to parse phrase, but optional is empty");
        }

        stack.add(parsedPhrase.get());
    }

    /**
     * Determine whether the given pattern could be used to reduce the stack
     * @param pattern the pattern to check
     * @return true if the pattern can reduce the stack, false otherwise
     */
    private boolean canReduce(List<PhraseElement> stack, Pattern pattern) {
        if (stack.size() < pattern.size()) {
            return false;
        }

        return pattern.canParse(peek(stack, pattern.size()));
    }

    private List<Pattern> reductionCandidates(List<PhraseElement> stack) {
        return patterns.stream()
                .filter(pattern -> this.canReduce(stack, pattern))
                .collect(Collectors.toList());
    }

    /**
     * @param n elements on top to return
     * @return a view into the top of th
     */
    private List<PhraseElement> peek(List<PhraseElement> stack, int n) {
        assert n > 0;
        assert n <= stack.size();
        return stack.subList(stack.size() - n, stack.size());
    }

    private List<PhraseElement> pop(List<PhraseElement> stack, int n) {
        List<PhraseElement> top = peek(stack, n);
        List<PhraseElement> pop = new ArrayList<>(top);
        top.clear();
        return pop;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        List<Pattern> patterns = new ArrayList<>();

        private Builder() {}

        public Builder addPattern(Pattern pattern) {
            this.patterns.add(pattern);
            return this;
        }

        public PhraseParser build() {
            return new PhraseParser(this);
        }
    }
}
