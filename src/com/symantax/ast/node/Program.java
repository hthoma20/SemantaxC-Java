package com.symantax.ast.node;

import com.symantax.ast.node.list.ModuleList;
import com.symantax.ast.visitor.ASTVisitor;
import lombok.Getter;

public class Program extends AstNode {

    @Getter
    private ModuleList modules;

    public Program(ModuleList modules) {
        super(null);
        this.modules = modules;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
