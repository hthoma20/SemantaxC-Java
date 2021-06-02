package com.symantax.dynamic.model.phrase;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PhraseWord implements PhraseElement {
    @Getter
    private String word;
}
