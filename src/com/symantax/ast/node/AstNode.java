package com.symantax.ast.node;

import com.symantax.ast.util.FilePos;
import com.symantax.ast.visitor.ASTVisitor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
public abstract class AstNode {

    @Getter
    @Setter
    private FilePos filePos;

    public abstract <T> T accept(ASTVisitor<T> visitor);
}
