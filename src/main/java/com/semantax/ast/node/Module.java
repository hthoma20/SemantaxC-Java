package com.semantax.ast.node;

import com.semantax.ast.node.list.ModuleList;
import com.semantax.ast.node.list.PatternDefinitionList;
import com.semantax.ast.node.list.StatementList;
import com.semantax.ast.node.list.WordList;
import com.semantax.ast.visitor.ASTVisitor;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;


@Builder(builderClassName = "Builder")
@Getter
public class Module extends AstNode {

    private final String modifier;
    private final String name;
    private final WordList modulesUsed;
    private final ModuleList subModules;
    private final StatementList statements;
    private final PatternDefinitionList patterns;


    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
