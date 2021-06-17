package com.semantax.dynamic.model.phrase;

import com.semantax.dynamic.model.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Expression implements PhraseElement {
    @Getter
    private Type type;

    @Override
    public String toString() {
        return String.format("Expression<%s>", type.getName());
    }
}
