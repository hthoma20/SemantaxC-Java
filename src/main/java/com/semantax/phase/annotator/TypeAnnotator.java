package com.semantax.phase.annotator;

import com.semantax.ast.node.Expression;
import com.semantax.ast.visitor.AstVisitor;


public interface TypeAnnotator extends AstVisitor<Boolean> {

    /**
     * Set the type attribute on the given expression, as well as all of its
     * subexpressions. This operation may fail due to an error in the program,
     * in this case, an appropriate error message is printed and false is returned.
     * If the expression is successfully annotated, then true is returned
     *
     * @param expression the expression to annotate
     * @return false if some error occurred, or true otherwise
     */
    boolean annotate(Expression expression);

}
