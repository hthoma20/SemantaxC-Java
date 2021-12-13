package com.semantax.phase.codegen;

import com.semantax.ast.node.Word;
import com.semantax.ast.node.pattern.PatternDefinition;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Stores names for types that need to be generated
 */
public class GeneratedPatternRegistry {

    private int patternIndex = 0;
    private final Map<PatternDefinition, String> patternNames = new HashMap<>();

    /**
     * Get the name that has been registered for the given pattern.
     * If no name exists, register one and return it
     * @param pattern the PatternDefinition to get a name for
     * @return the name of the pattern that should be used in generated code
     */
    public String getPatternName(PatternDefinition pattern) {
        return patternNames.computeIfAbsent(pattern, p ->
                String.format("pattern_%s_%d", getPatternHint(pattern), patternIndex++));
    }

    /**
     * Compute a short, descriptive string for this pattern
     * not garunteed to be unique
     * @param pattern the pattern to describe
     * @return a shot, descriptive string
     */
    private String getPatternHint(PatternDefinition pattern) {
        int maxHintLength = 8;
        String fullPatternHint = pattern.getSyntax()
                .stream()
                .map(Word::getValue)
                .filter(word -> word.matches("[a-zA-Z]+"))
                .collect(Collectors.joining(""));
        return fullPatternHint.length() <= maxHintLength ?
                fullPatternHint : fullPatternHint.substring(0, maxHintLength);
    }
}
