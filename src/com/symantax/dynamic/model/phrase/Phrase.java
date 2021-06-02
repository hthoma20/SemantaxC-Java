package com.symantax.dynamic.model.phrase;

import java.util.ArrayList;
import java.util.List;

public class Phrase {

    private final List<PhraseElement> elements;

    private Phrase(Builder builder) {
        this.elements = builder.elements;
    }

    public List<PhraseElement> cloneElements() {
        return new ArrayList<>(elements);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private List<PhraseElement> elements = new ArrayList<>();

        private Builder() {}

        public Builder addElement(PhraseElement element) {
            this.elements.add(element);
            return this;
        }

        public Phrase build() {
            return new Phrase(this);
        }
    }

}
