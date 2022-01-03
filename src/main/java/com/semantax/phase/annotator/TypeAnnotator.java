package com.semantax.phase.annotator;

import com.semantax.ast.node.AstNode;

public interface TypeAnnotator {
    /**
     * For each child/descendant that is an Expression, annotate the Expression with
     * its type. This operation may fail due to an error in the program,
     * in this case, an appropriate error message is printed and false is returned.
     * If the expression is successfully annotated, then true is returned from the visit method
     * @param node the node to annotate
     * @return false if there was some error, true otherwise
     */
    boolean annotate(AstNode node);
}
