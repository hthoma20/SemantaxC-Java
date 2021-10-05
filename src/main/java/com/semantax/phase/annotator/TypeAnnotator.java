package com.semantax.phase.annotator;

import com.semantax.ast.node.Expression;
import com.semantax.ast.visitor.AstVisitor;

/**
 *
 * When visit is called on a given AstNode, visit each of that node's children.
 * For each child/descendant that is an Expression, annotate the Expression with
 * its type. This operation may fail due to an error in the program,
 * in this case, an appropriate error message is printed and false is returned.
 * If the expression is successfully annotated, then true is returned from the visit method
 */
public interface TypeAnnotator extends AstVisitor<Boolean> {
}
