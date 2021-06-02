package com.symantax.dynamic.model.pattern;

import com.symantax.dynamic.model.Type;
import com.symantax.dynamic.model.phrase.Expression;
import com.symantax.dynamic.model.phrase.PhraseElement;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
public class Variable implements PatternElement {
    private String name;
    private Type type;


    @Override
    public boolean matches(PhraseElement phraseElement) {
        if (!(phraseElement instanceof Expression)) {
            return false;
        }

        return type.equals(((Expression) phraseElement).getType());
    }

    @Override
    public String toString() {
        return String.format("<%s:%s>", name, type.getName());
    }
}
