package com.semantax.ast.util.eventual;

import java.util.Optional;

/**
 * A value that will eventually be known at some future phase of the compiler
 * This class can be used to represent data that is, so far, unfulfilled. It can
 * also be used to assure that data is present before it is used.
 *
 * @param <T> The type that will eventually be fulfilled
 */
public class Eventual<T> {
    // empty if unfulfilled
    private Optional<T> data = Optional.empty();

    private Eventual() { }

    public static <T> Eventual<T> unfulfilled() {
        return new Eventual<>();
    }

    public void fulfill(T data) {

        if (this.data.isPresent()) {
            throw FulfilledException.of("Attempt to re-fulfill %s", toString());
        }

        this.data = Optional.of(data);
    }

    public T get() {
        return data.orElseThrow(() ->
                UnfulfilledException.of("Attempt to get unfulfilled Eventual"));
    }

    public boolean isFulfilled() {
        return data.isPresent();
    }

    @Override
    public String toString() {
        return data
                .map(d -> String.format("Eventual[%s]", d))
                .orElse("Eventual.unfulfilled");
    }
}
