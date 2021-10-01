package com.semantax.phase.annotator;

import com.semantax.ast.type.Type;

/**
 * Determines whether types are assignable
 */
public interface TypeAssignabilityChecker {

    /**
     * Determines whether a value with type rhs can be assigned to a
     * variable of type lhs
     * @param lhs the "assign to" type
     * @param rhs the "assign from" type
     * @return whether lhs is assignable to rhs
     */
    boolean isAssignable(Type lhs, Type rhs);

}
