package com.symantax.ast.node.list;

import com.symantax.ast.node.Word;

import java.util.stream.Collectors;

public class WordList extends AstNodeList<Word> {
    @Override
    public String toString() {
        return nodes.stream().map(Word::toString).collect(Collectors.joining(" "));
    }
}
