package com.semantax.ast.node;

import com.semantax.ast.visitor.ASTVisitor;

/**
 * The definition of a pattern, i.e.
 * <pre>
 * {@code
 *      $a + b$
 *      (a: int, b: int) -> int {
 *          \@addint(a, b)
 *      }
 * }
 * </pre>
 *
 */
public class PatternDef extends AstNode {

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
