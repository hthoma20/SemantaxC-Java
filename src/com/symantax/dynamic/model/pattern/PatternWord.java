package com.symantax.dynamic.model.pattern;

import com.symantax.dynamic.model.phrase.PhraseElement;
import com.symantax.dynamic.model.phrase.PhraseWord;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
public class PatternWord implements PatternElement {

    @Getter
    private String word;

    @Override
    public boolean matches(PhraseElement phraseElement) {
        if (!(phraseElement instanceof PhraseWord)) {
            return false;
        }

        return (word.equals(((PhraseWord) phraseElement).getWord()));
    }

    @Override
    public String toString() {
        return word;
    }
}
