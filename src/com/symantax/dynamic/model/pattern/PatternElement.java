package com.symantax.dynamic.model.pattern;

import com.symantax.dynamic.model.phrase.PhraseElement;

public interface PatternElement {
    boolean matches(PhraseElement phraseElement);
}
