package com.semantax.ast.node;

import com.semantax.ast.node.list.ModuleList;
import com.semantax.ast.node.progcall.DeclProgCall;
import com.semantax.ast.util.eventual.Eventual;
import com.semantax.ast.visitor.AstVisitor;
import lombok.Getter;

import java.util.Set;

public class Program extends AstNode {

    @Getter
    private final ModuleList modules;

    private final Eventual<Set<DeclProgCall>> globalVariables = Eventual.unfulfilled();

    public Program(ModuleList modules) {
        super(null);
        this.modules = modules;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Set<DeclProgCall> getGlobalVariables() {
        return globalVariables.get();
    }

    public void setGlobalVariables(Set<DeclProgCall> globalVariables) {
        this.globalVariables.fulfill(globalVariables);
    }
}
