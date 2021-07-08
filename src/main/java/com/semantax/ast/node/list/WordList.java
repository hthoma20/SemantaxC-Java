package com.semantax.ast.node.list;

import com.semantax.ast.node.Word;

import java.util.stream.Collectors;

public class WordList extends AstNodeList<Word> {
    @Override
    public String toString() {
        return nodes.stream().map(Word::toString).collect(Collectors.joining(" "));
    }
}
