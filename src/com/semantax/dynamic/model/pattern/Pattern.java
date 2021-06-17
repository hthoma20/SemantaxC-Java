package com.semantax.dynamic.model.pattern;

import com.semantax.dynamic.model.Type;
import com.semantax.dynamic.model.parsed.ParsedPhrase;
import com.semantax.dynamic.model.phrase.PhraseElement;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

public class Pattern implements Iterable<PatternElement> {
    @Getter
    private final List<PatternElement> elements;
    @Getter
    private final Type type;

    private Pattern(Builder builder) {
        this.elements = builder.elements;
        this.type = builder.type;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * If possible (i.e canParse returns true), parse the given elements into a ParsePhrase
     * If not, return Optional.empty()
     * @param phrase the elements to parse
     * @return the parsed phrase, or empty if this pattern cannot parse the given phrase
     */
    public Optional<ParsedPhrase> parse(List<PhraseElement> phrase) {

        if (!canParse(phrase)) {
            return Optional.empty();
        }

        return Optional.of(ParsedPhrase.builder()
                .withElements(phrase)
                .withType(this.type)
                .build());
    }

    /**
     * Determine whether this pattern can parse the list of phrase elements
     * @param phrase the elements to check
     * @return whether this pattern can parse this phrase
     */
    public boolean canParse(List<PhraseElement> phrase) {
        if (phrase.size() != elements.size()) {
            return false;
        }

        for (int i = 0; i < elements.size(); i++) {
            if (!elements.get(i).matches(phrase.get(i))) {
                return false;
            }
        }

        return true;
    }

    public int size() {
        return elements.size();
    }

    @Override
    public Iterator<PatternElement> iterator() {
        return elements.iterator();
    }

    @Override
    public String toString() {
        return String.format("$%s$", elements.stream().map(Objects::toString).collect(Collectors.joining(" ")));
    }

    public static class Builder {

        private List<PatternElement> elements = new ArrayList<>();
        private Type type;

        private Builder() {}

        public Builder withType(Type type) {
            this.type = type;
            return this;
        }

        public Builder addElement(PatternElement element) {
            this.elements.add(element);
            return this;
        }

        public Pattern build() {
            return new Pattern(this);
        }
    }

}
