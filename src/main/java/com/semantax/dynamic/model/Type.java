package com.semantax.dynamic.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

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
