package com.semantax.ast.node;

import com.semantax.ast.type.Type;
import com.semantax.ast.util.eventual.Eventual;

public interface VariableDeclaration {
    String getDeclName();
    Type getDeclType();
}
