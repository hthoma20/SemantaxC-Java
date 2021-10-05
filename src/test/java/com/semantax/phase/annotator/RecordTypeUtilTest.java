package com.semantax.phase.annotator;

import com.semantax.ast.node.ParsableExpression;
import com.semantax.ast.node.literal.BoolLit;
import com.semantax.ast.node.literal.IntLit;
import com.semantax.ast.node.literal.RecordLit;
import com.semantax.ast.node.literal.type.BoolTypeLit;
import com.semantax.ast.node.literal.type.IntTypeLit;
import com.semantax.ast.node.literal.type.RecordTypeLit;
import junit.framework.TestCase;

import java.util.Set;

import static com.semantax.testutil.AstUtil.asList;
import static com.semantax.testutil.AstUtil.pair;
import static com.semantax.testutil.AstUtil.parsedTo;

public class RecordTypeUtilTest extends TestCase {
    private final RecordTypeUtil recordTypeUtil = new RecordTypeUtil();

    public void test_getDuplicateNames_lit_withDuplicates() {
        // given
        RecordLit recordLit = RecordLit.builder()
                .nameParsableExpressionPairs(asList(
                        pair("a", parsedTo(new IntLit(5))),
                        pair("b", parsedTo(new IntLit(10))),
                        pair("a", parsedTo(new BoolLit(true)))))
                .build();

        // when
        Set<String> duplicateNames = recordTypeUtil.getDuplicateNames(recordLit);

        // then
        assertEquals(1, duplicateNames.size());
        assertTrue(duplicateNames.contains("a"));
    }

    public void test_getDuplicateNames_typeLit_withDuplicates() {
        // given
        RecordTypeLit recordTypeLit = RecordTypeLit.builder()
                .nameTypeLitPairs(asList(
                        pair("a", new IntTypeLit()),
                        pair("b", new IntTypeLit()),
                        pair("a", new BoolTypeLit())))
                .build();

        // when
        Set<String> duplicateNames = recordTypeUtil.getDuplicateNames(recordTypeLit);

        // then
        assertEquals(1, duplicateNames.size());
        assertTrue(duplicateNames.contains("a"));
    }

    public void test_getDuplicateNames_lit_withNoDuplicates() {
        // given
        RecordLit recordLit = RecordLit.builder()
                .nameParsableExpressionPairs(asList(
                        pair("a", parsedTo(new IntLit(5))),
                        pair("b", parsedTo(new IntLit(10)))))
                .build();

        // when
        Set<String> duplicateNames = recordTypeUtil.getDuplicateNames(recordLit);

        // then
        assertEquals(0, duplicateNames.size());
    }

    public void test_getDuplicateNames_typeLit_withNoDuplicates() {
        // given
        RecordTypeLit recordTypeLit = RecordTypeLit.builder()
                .nameTypeLitPairs(asList(
                        pair("a", new IntTypeLit()),
                        pair("b", new IntTypeLit())))
                .build();

        // when
        Set<String> duplicateNames = recordTypeUtil.getDuplicateNames(recordTypeLit);

        // then
        assertEquals(0, duplicateNames.size());
    }

}