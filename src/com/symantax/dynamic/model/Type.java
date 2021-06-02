package com.symantax.dynamic.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
public class Type {

    @Getter
    private String name;

    @Override
    public String toString() {
        return String.format("Type<%s>", name);
    }

}
