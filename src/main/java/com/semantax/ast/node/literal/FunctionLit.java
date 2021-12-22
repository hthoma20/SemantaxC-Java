package com.semantax.ast.node.literal;

import com.semantax.ast.node.ParsableExpression;
import com.semantax.ast.node.VariableReference;
import com.semantax.ast.node.list.StatementList;
import com.semantax.ast.node.progcall.DeclProgCall;
import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.AstVisitor;
import com.semantax.ast.node.literal.type.RecordTypeLit;
import com.semantax.ast.node.literal.type.TypeLit;
import lombok.Builder;
import lombok.Getter;


import java.util.LinkedHashSet;
import java.util.Optional;


@Builder(builderClassName = "Builder")
@Getter
public class FunctionLit extends Literal {
    @lombok.Builder.Default
    private RecordTypeLit input = RecordTypeLit.EMPTY_RECORD;
    @lombok.Builder.Default
    private Optional<TypeLit> output = Optional.empty();

    // A function will either be a list of statements, or a single expression
    @lombok.Builder.Default
    private Optional<StatementList> statements = Optional.empty();
    @lombok.Builder.Default
    private Optional<ParsableExpression> returnExpression = Optional.empty();

    @Getter
    private final LinkedHashSet<DeclProgCall> localVariables = new LinkedHashSet<>();

    // Enclosed variables in this function, from the perspective of the enclosing scope
    @Getter
    private final LinkedHashSet<VariableReference> enclosedVariables = new LinkedHashSet<>();

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public static class Builder {

        public FunctionLit buildWith(FilePos filePos) {
            FunctionLit functionLit = build();
            functionLit.setFilePos(filePos);
            return functionLit;
        }

    }

    public void addLocalVariable(DeclProgCall declaration) {
        localVariables.add(declaration);
    }

    public void addEnclosedVariable(VariableReference variable) {
        enclosedVariables.add(variable);
    }
}
