package com.semantax.phase.parser;

import com.semantax.ast.node.Expression;
import com.semantax.ast.node.ParsableExpression;
import com.semantax.ast.node.VariableDeclaration;
import com.semantax.ast.node.VariableReference;
import com.semantax.ast.node.pattern.PatternDefinition;
import com.semantax.ast.node.pattern.PatternInvocation;
import com.semantax.ast.node.PhraseElement;
import com.semantax.ast.node.Word;
import com.semantax.ast.type.Type;
import com.semantax.exception.CompilerException;
import com.semantax.phase.SymbolTable;
import com.semantax.phase.annotator.TypeAssignabilityChecker;
import lombok.AllArgsConstructor;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
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
    public List<PatternElement> patternElements(PatternDefinition pattern, SymbolTable symbolTable) {
        Set<String> variables = pattern.getVariables();
        return pattern.getSyntax().stream()
                .map(word -> variables.contains(word.getValue()) ?
                        new VariableElement(pattern.getType(word.getValue()), symbolTable) :
                        new WordElement(word))
                .collect(Collectors.toList());
    }

    /**
     * @param phrase the phrase to check
     * @param pattern the pattern to check against
     * @param symbolTable the symbol table in which to lookup words
     * @return whether the given phrase matches the given pattern
     */
    public boolean matches(List<PhraseElement> phrase, PatternDefinition pattern, SymbolTable symbolTable) {
        if (phrase.size() != pattern.getSyntax().size()) {
            return false;
        }

        List<PatternElement> patternElements = patternElements(pattern, symbolTable);

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
     * @param symbolTable the symbol table to lookup variables
     * @return a pattern invocation for the given pattern and the given phrase
     */
    public PatternInvocation parse(List<PhraseElement> phrase, PatternDefinition pattern, SymbolTable symbolTable) {

        if (!pattern.getSemantics().getOutput().isPresent()) {
            throw CompilerException.of("Unexpected call without pattern return type");
        }

        Set<String> variables = pattern.getVariables();

        PatternInvocation.Builder invocationBuilder = PatternInvocation.builder()
                .patternDefinition(pattern);

        for (int i = 0; i < phrase.size(); i++) {

            PhraseElement phraseElement = phrase.get(i);
            Word patternElement = pattern.getSyntax().get(i);

            // if this position in the pattern is a variable
            if (variables.contains(patternElement.getValue())) {

                Expression argument;

                if (phraseElement instanceof Expression) {
                    argument = (Expression) phraseElement;
                }
                else if (phraseElement instanceof ParsableExpression) {
                    argument = ((ParsableExpression) phraseElement).getExpression();
                }
                else if (phraseElement instanceof Word) {
                    argument = VariableReference.builder()
                            .declaration(symbolTable.lookup(((Word) phraseElement).getValue()).get())
                            .buildWith(phraseElement.getFilePos());
                }
                else {
                    throw CompilerException.of("Unexpected phrase element: [%s]", phraseElement);
                }

                invocationBuilder.argument(patternElement.getValue(), argument);
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
        private final SymbolTable symbolTable;

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
            else if (phraseElement instanceof Word) {
                Optional<VariableDeclaration> variable = symbolTable.lookup(((Word) phraseElement).getValue());
                if (!variable.isPresent()) {
                    return false;
                }
                phraseElementType = variable.get().getDeclType();
            }
            else {
                return false;
            }

            return typeAssignabilityChecker.isAssignable(this.type, phraseElementType);
        }

    }

}
