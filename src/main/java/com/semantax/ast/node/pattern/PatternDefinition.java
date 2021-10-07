package com.semantax.ast.node.pattern;

import com.semantax.ast.node.AstNode;
import com.semantax.ast.node.list.WordList;
import com.semantax.ast.node.literal.FunctionLit;
import com.semantax.ast.node.literal.type.NameTypeLitPair;
import com.semantax.ast.type.Type;
import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.AstVisitor;
import com.semantax.exception.CompilerException;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Builder(builderClassName = "Builder")
@Getter
public class PatternDefinition extends AstNode {

    private final WordList syntax;
    private final FunctionLit semantics;

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public static class Builder {
        public PatternDefinition buildWith(FilePos filePos) {
            PatternDefinition patternDef = build();
            patternDef.setFilePos(filePos);
            return patternDef;
        }
    }

    /**
     * @return the words in this pattern's syntax which are variables
     */
    public Set<String> getVariables() {
        return semantics.getInput().getNameTypeLitPairs().stream()
                .map(NameTypeLitPair::getName)
                .collect(Collectors.toSet());
    }

    public Type getType(String variable) {
        for (NameTypeLitPair nameTypeLitPair : semantics.getInput().getNameTypeLitPairs()) {
            if (nameTypeLitPair.getName().equals(variable)) {
                return nameTypeLitPair.getType().getRepresentedType();
            }
        }
        throw CompilerException.of("variable [%s] not present", variable);
    }

    @Override
    public String toString() {
        return String.format("$%s$", syntax);
    }
}
