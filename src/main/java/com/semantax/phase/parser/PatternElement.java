package com.semantax.phase.parser;

import com.semantax.ast.node.PhraseElement;

public interface PatternElement {
    boolean matches(PhraseElement phraseElement);
}
