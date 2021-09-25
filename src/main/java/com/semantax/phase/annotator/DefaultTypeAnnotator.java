package com.semantax.phase.annotator;

import com.semantax.ast.node.Expression;
import com.semantax.ast.node.literal.IntLit;
import com.semantax.ast.type.IntType;
import com.semantax.ast.visitor.TraversalVisitor;

import javax.inject.Inject;

public class DefaultTypeAnnotator extends TraversalVisitor<Boolean> implements TypeAnnotator  {

    @Inject
    public DefaultTypeAnnotator() {
    }

    @Override
    public boolean annotate(Expression expression) {
        return expression.accept(this);
    }

    @Override
    public Boolean visit(IntLit intLit) {
        intLit.setType(IntType.INT_TYPE);
        return true;
    }
}
