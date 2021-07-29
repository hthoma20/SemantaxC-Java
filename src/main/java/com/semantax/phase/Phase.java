package com.semantax.phase;

import java.util.Optional;

/**
 * Represents a phase of compilation. A Phase is essentially a function
 * from some input (i.e an Ast) to some output (i.e an altered Ast).
 *
 * A phase may mutate the input itself and simply return the mutated object.
 * Therefore, callers shouldn't rely on the input object being unchanged.
 *
 * @param <I> the input type
 * @param <O> the output type
 */
public interface Phase<I, O> {
    Optional<O> process(I input);
}
