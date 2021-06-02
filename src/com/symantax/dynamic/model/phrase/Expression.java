package com.symantax.dynamic.model.phrase;

import com.symantax.dynamic.model.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
public class Expression implements PhraseElement {
    @Getter
    private Type type;

    @Override
    public String toString() {
        return String.format("Expression<%s>", type.getName());
    }
}
