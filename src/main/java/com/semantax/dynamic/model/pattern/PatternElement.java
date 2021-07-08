package com.semantax.dynamic.model.pattern;

import com.semantax.dynamic.model.phrase.PhraseElement;

public interface PatternElement {
    boolean matches(PhraseElement phraseElement);
}
