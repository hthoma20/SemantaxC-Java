package com.symantax.ast.node;

import com.symantax.ast.node.list.ModuleList;
import com.symantax.ast.node.list.StatementList;
import com.symantax.ast.node.list.WordList;

import com.symantax.ast.visitor.ASTVisitor;
import lombok.Builder;
import lombok.Getter;


@Builder
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
