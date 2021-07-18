package com.semantax.ast.node;

import com.semantax.ast.node.list.NameTypePairList;
import com.semantax.ast.node.list.WordList;
import com.semantax.ast.node.literal.FunctionLit;
import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.ASTVisitor;
import lombok.Builder;

@Builder(builderClassName = "Builder")
public class PatternDefinition extends AstNode {

    private final WordList syntax;
    private final FunctionLit semantics;

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public static class Builder {
        public PatternDefinition buildWith(FilePos filePos) {
            PatternDefinition patternDef = build();
            patternDef.setFilePos(filePos);
            return patternDef;
        }
    }
}
