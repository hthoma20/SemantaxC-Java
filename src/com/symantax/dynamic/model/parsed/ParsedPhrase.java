package com.symantax.dynamic.model.parsed;

import com.symantax.dynamic.model.Type;
import com.symantax.dynamic.model.phrase.Expression;
import com.symantax.dynamic.model.phrase.PhraseElement;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class ParsedPhrase extends Expression {

    private List<PhraseElement> parseTree;

    public ParsedPhrase(Builder builder) {
        super(builder.type);
        this.parseTree = builder.parseTree;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<PhraseElement> parseTree = new ArrayList<>();
        private Type type;

        private Builder() {}

        public Builder withElements(List<PhraseElement> elements) {
            this.parseTree.addAll(elements);
            return this;
        }

        public Builder withType(Type type) {
            this.type = type;
            return this;
        }

        public ParsedPhrase build() {
            return new ParsedPhrase(this);
        }
    }
}
