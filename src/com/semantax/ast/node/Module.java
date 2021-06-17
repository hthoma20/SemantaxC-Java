package com.semantax.ast.node;

import com.semantax.ast.node.list.ModuleList;
import com.semantax.ast.node.list.StatementList;
import com.semantax.ast.node.list.WordList;

import com.semantax.ast.visitor.ASTVisitor;
import lombok.Builder;
import lombok.Getter;


@Builder(builderClassName = "Builder")
@Getter
public class Module extends AstNode {

    private String modifier;
    private String name;
    private WordList modulesUsed;
    private ModuleList subModules;
    private StatementList statements;


    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
