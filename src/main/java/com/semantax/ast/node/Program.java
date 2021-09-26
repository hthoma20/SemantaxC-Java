package com.semantax.ast.node;

import com.semantax.ast.node.list.ModuleList;
import com.semantax.ast.visitor.AstVisitor;
import lombok.Getter;

public class Program extends AstNode {

    @Getter
    private final ModuleList modules;

    public Program(ModuleList modules) {
        super(null);
        this.modules = modules;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
