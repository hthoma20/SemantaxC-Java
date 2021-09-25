package com.semantax.ast.node;

import com.semantax.ast.node.literal.IntLit;
import com.semantax.ast.util.FilePos;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PhraseTest extends TestCase {

    public void test_subExpressions() {
        // test the phrase `let x = (1 + 2) + 3`

        //given
        Expression one = new IntLit(1);
        Expression two = new IntLit(2);
        Expression three = new IntLit(3);

        ParsableExpression onePlusTwo = ParsableExpression.fromPhrase(Phrase.builder()
                .element(one)
                .element(new Word(FilePos.none(), "+"))
                .element(two)
                .build());

        Phrase phrase = Phrase.builder()
                .element(new Word(FilePos.none(), "let"))
                .element(new Word(FilePos.none(), "x"))
                .element(new Word(FilePos.none(), "="))
                .element(onePlusTwo)
                .element(new Word(FilePos.none(), "+"))
                .element(three)
                .build();

        // when
        List<Expression> subExpressions = phrase.subExpressions();

        // then
        assertEquals(Collections.singletonList(three), subExpressions);

    }
}