package com.semantax.ast.node.list;

import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.ASTVisitor;
import com.semantax.ast.node.AstNode;
import com.semantax.exception.CompilerException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public abstract class AstNodeList<NodeType extends AstNode> extends AstNode
        implements Iterable<NodeType> {

    protected List<NodeType> nodes = new ArrayList<>();

    @Override
    public Iterator<NodeType> iterator() {
        return nodes.iterator();
    }

    public void add(NodeType node) {
        nodes.add(node);
    }

    public void addAll(AstNodeList<NodeType> astNodeList) {
        for (NodeType node : astNodeList) {
            add(node);
        }
    }

    public void replace(NodeType node, AstNodeList<NodeType> replacements) {
        int index = nodes.indexOf(node);

        if (index == -1) {
            throw CompilerException.of("Attempt to replace missing node: %s", node);
        }

        nodes.remove(index);

        for (NodeType replacement : replacements) {
            nodes.add(index, replacement);
            index++;
        }
    }

    public int size() {
        return nodes.size();
    }

    public Stream<NodeType> stream() {
        return nodes.stream();
    }

    @Override
    public FilePos getFilePos() {

        FilePos myFilePos = super.getFilePos();

        if (myFilePos == null && !nodes.isEmpty()) {
            return nodes.get(0).getFilePos();
        }

        return myFilePos;
    }


    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
