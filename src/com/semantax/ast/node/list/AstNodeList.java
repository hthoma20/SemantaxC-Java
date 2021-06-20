package com.semantax.ast.node.list;

import com.semantax.ast.node.AstNode;
import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.ASTVisitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public int size() {
        return nodes.size();
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
