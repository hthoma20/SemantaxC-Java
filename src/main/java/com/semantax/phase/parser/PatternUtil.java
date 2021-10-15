package com.semantax.phase.parser;

import com.semantax.ast.node.Expression;
import com.semantax.ast.node.ParsableExpression;
import com.semantax.ast.node.pattern.PatternDefinition;
import com.semantax.ast.node.pattern.PatternInvocation;
import com.semantax.ast.node.PhraseElement;
import com.semantax.ast.node.Word;
import com.semantax.ast.type.Type;
import com.semantax.exception.CompilerException;
import com.semantax.phase.annotator.TypeAssignabilityChecker;
import lombok.AllArgsConstructor;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PatternUtil {

    private final TypeAssignabilityChecker typeAssignabilityChecker;

    @Inject
    public PatternUtil(TypeAssignabilityChecker typeAssignabilityChecker) {
        this.typeAssignabilityChecker = typeAssignabilityChecker;
    }

    /**
     * @param pattern the pattern to get elements for
     * @return a list of PatternElements to be used to determine a match
     */
    public List<PatternElement> patternElements(PatternDefinition pattern) {
        Set<String> variables = pattern.getVariables();
        return pattern.getSyntax().stream()
                .map(word -> variables.contains(word.getValue()) ?
                        new VariableElement(pattern.getType(word.getValue())) :
                        new WordElement(word))
                .collect(Collectors.toList());
    }

    /**
     * @param phrase the phrase to check
     * @param pattern the pattern to check against
     * @return whether the given phrase matches the given pattern
     */
    public boolean matches(List<PhraseElement> phrase, PatternDefinition pattern) {
        if (phrase.size() != pattern.getSyntax().size()) {
            return false;
        }

        List<PatternElement> patternElements = patternElements(pattern);

        for (int i = 0; i < phrase.size(); i++) {
            if (!patternElements.get(i).matches(phrase.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Prerequisite is that the pattern can actually parse the given phrase
     *
     * @param phrase the phrase to parse
     * @param pattern the pattern to parse with
     * @return a pattern invocation for the given pattern and the given phrase
     */
    public PatternInvocation parse(List<PhraseElement> phrase, PatternDefinition pattern) {

        if (!pattern.getSemantics().getOutput().isPresent()) {
            throw CompilerException.of("Unexpected call without pattern return type");
        }

        Set<String> variables = pattern.getVariables();

        PatternInvocation.Builder invocationBuilder = PatternInvocation.builder()
                .patternDefinition(pattern);

        for (int i = 0; i < phrase.size(); i++) {
            String patternWord = pattern.getSyntax().get(i).getValue();
            if (variables.contains(patternWord)) {
                Expression argument = phrase.get(i) instanceof Expression ?
                        (Expression) phrase.get(i) : ((ParsableExpression) phrase.get(i)).getExpression();
                invocationBuilder.argument(patternWord, argument);
            }
        }

        PatternInvocation patternInvocation = invocationBuilder.build();
        patternInvocation.setType(pattern.getSemantics().getOutput().get().getRepresentedType());
        patternInvocation.setFilePos(phrase.get(0).getFilePos());
        return patternInvocation;
    }

    @AllArgsConstructor
    private static class WordElement implements PatternElement {
        private final Word word;

        @Override
        public boolean matches(PhraseElement phraseElement) {
            if (!(phraseElement instanceof Word)) {
                return false;
            }

            return this.word.getValue().equals(((Word) phraseElement).getValue());
        }
    }

    @AllArgsConstructor
    private class VariableElement implements PatternElement {
        private final Type type;

        @Override
        public boolean matches(PhraseElement phraseElement) {
            Type phraseElementType;

            // TODO: can this be simplified to only include Expression or ParsableExpression?
            if (phraseElement instanceof Expression) {
                phraseElementType = ((Expression) phraseElement).getType();
            }
            else if (phraseElement instanceof ParsableExpression) {
                phraseElementType = ((ParsableExpression) phraseElement).getExpression().getType();
            }
            else {
                return false;
            }

            return typeAssignabilityChecker.isAssignable(this.type, phraseElementType);
        }

    }

}
