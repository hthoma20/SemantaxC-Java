package com.semantax.ast.node;

import com.semantax.ast.util.FilePos;

/**
 * Marker interface to indicate that the implementor can be part of a phrase
 * Should only be implemented by @link{Expression}, {@link ParsableExpression} and {@link Word}
 */
public interface PhraseElement {
    FilePos getFilePos();
}
