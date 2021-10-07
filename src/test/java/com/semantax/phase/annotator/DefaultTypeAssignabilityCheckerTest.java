package com.semantax.phase.annotator;

import com.semantax.ast.node.list.NameTypePairList;
import com.semantax.ast.type.ArrayType;
import com.semantax.ast.type.FuncType;
import com.semantax.ast.type.RecordType;
import com.semantax.exception.CompilerException;
import junit.framework.TestCase;

import java.util.Optional;

import static com.semantax.ast.type.BoolType.BOOL_TYPE;
import static com.semantax.ast.type.IntType.INT_TYPE;
import static com.semantax.ast.type.StringType.STRING_TYPE;
import static com.semantax.ast.type.VoidType.VOID_TYPE;
import static com.semantax.testutil.AstUtil.asList;
import static com.semantax.testutil.AstUtil.pair;
import static org.junit.Assert.assertThrows;

public class DefaultTypeAssignabilityCheckerTest extends TestCase {

    private final DefaultTypeAssignabilityChecker assignabilityChecker = new DefaultTypeAssignabilityChecker();

    public void test_primitiveToPrimitive_positive() {
        assertTrue(assignabilityChecker.isAssignable(INT_TYPE, INT_TYPE));
        assertTrue(assignabilityChecker.isAssignable(BOOL_TYPE, BOOL_TYPE));
        assertTrue(assignabilityChecker.isAssignable(STRING_TYPE, STRING_TYPE));
    }

    public void test_primitiveToPrimitive_negative() {
        assertFalse(assignabilityChecker.isAssignable(INT_TYPE, BOOL_TYPE));
        assertFalse(assignabilityChecker.isAssignable(BOOL_TYPE, INT_TYPE));
        assertFalse(assignabilityChecker.isAssignable(INT_TYPE, STRING_TYPE));
        assertFalse(assignabilityChecker.isAssignable(STRING_TYPE, BOOL_TYPE));
    }

    public void test_arrayToArray_positive() {
        assertTrue(assignabilityChecker.isAssignable(
                ArrayType.builder().subType(INT_TYPE).build(),
                ArrayType.builder().subType(INT_TYPE).build()));

        // void type implies empty array - an empty array could be of any type
        assertTrue(assignabilityChecker.isAssignable(
                ArrayType.builder().subType(INT_TYPE).build(),
                ArrayType.builder().subType(VOID_TYPE).build()));
    }

    public void test_arrayToArray_negative() {
        assertFalse(assignabilityChecker.isAssignable(
                ArrayType.builder().subType(BOOL_TYPE).build(),
                ArrayType.builder().subType(INT_TYPE).build()));

        assertFalse(assignabilityChecker.isAssignable(
                ArrayType.builder().subType(INT_TYPE).build(),
                ArrayType.builder().subType(BOOL_TYPE).build()));

        // no lhs can have type void
        assertThrows(CompilerException.class, () -> assignabilityChecker.isAssignable(
                ArrayType.builder().subType(VOID_TYPE).build(),
                ArrayType.builder().subType(INT_TYPE).build()));
    }

    public void test_recordToRecord_positive() {
        // empty records
        assertTrue(assignabilityChecker.isAssignable(
                RecordType.builder().nameTypePairs(new NameTypePairList()).build(),
                RecordType.builder().nameTypePairs(new NameTypePairList()).build()));

        // matching names and types
        assertTrue(assignabilityChecker.isAssignable(
                RecordType.builder().nameTypePairs(asList(NameTypePairList.class,
                        pair("x", INT_TYPE),
                        pair("y", BOOL_TYPE))).build(),
                RecordType.builder().nameTypePairs(asList(NameTypePairList.class,
                        pair("x", INT_TYPE),
                        pair("y", BOOL_TYPE))).build()));
    }

    public void test_recordToRecord_negative() {

        // matching names, but not types
        assertFalse(assignabilityChecker.isAssignable(
                RecordType.builder().nameTypePairs(asList(NameTypePairList.class,
                        pair("x", INT_TYPE),
                        pair("y", INT_TYPE))).build(),
                RecordType.builder().nameTypePairs(asList(NameTypePairList.class,
                        pair("x", INT_TYPE),
                        pair("y", BOOL_TYPE))).build()));

        // matching types, but not names
        assertFalse(assignabilityChecker.isAssignable(
                RecordType.builder().nameTypePairs(asList(NameTypePairList.class,
                        pair("x", INT_TYPE),
                        pair("y", BOOL_TYPE))).build(),
                RecordType.builder().nameTypePairs(asList(NameTypePairList.class,
                        pair("x1", INT_TYPE),
                        pair("y1", BOOL_TYPE))).build()));

        // matching, but one is longer
        assertFalse(assignabilityChecker.isAssignable(
                RecordType.builder().nameTypePairs(asList(NameTypePairList.class,
                        pair("x", INT_TYPE),
                        pair("y", BOOL_TYPE))).build(),
                RecordType.builder().nameTypePairs(asList(NameTypePairList.class,
                        pair("x", INT_TYPE),
                        pair("y", BOOL_TYPE),
                        pair("x", STRING_TYPE))).build()));
    }

    public void test_functionToFunction_positive() {
        assertTrue(assignabilityChecker.isAssignable(
                FuncType.builder()
                    .inputType(INT_TYPE)
                    .outputType(Optional.of(BOOL_TYPE)).build(),
                FuncType.builder()
                    .inputType(INT_TYPE)
                    .outputType(Optional.of(BOOL_TYPE)).build()));
    }

    public void test_functionToFunction_negative() {
        // input mismatch
        assertFalse(assignabilityChecker.isAssignable(
                FuncType.builder()
                        .inputType(INT_TYPE)
                        .outputType(Optional.of(BOOL_TYPE)).build(),
                FuncType.builder()
                        .inputType(STRING_TYPE)
                        .outputType(Optional.of(BOOL_TYPE)).build()));

        // output mismatch
        assertFalse(assignabilityChecker.isAssignable(
                FuncType.builder()
                        .inputType(INT_TYPE)
                        .outputType(Optional.of(BOOL_TYPE)).build(),
                FuncType.builder()
                        .inputType(INT_TYPE)
                        .outputType(Optional.of(STRING_TYPE)).build()));
    }
}