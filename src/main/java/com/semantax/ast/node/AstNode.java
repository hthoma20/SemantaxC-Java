package com.semantax.ast.node;

import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.AstVisitor;
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

    public abstract <T> T accept(AstVisitor<T> visitor);
}
