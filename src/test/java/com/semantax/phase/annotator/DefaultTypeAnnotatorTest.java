package com.semantax.phase.annotator;

import com.semantax.ast.node.ParsableExpression;
import com.semantax.ast.node.list.NameParsableExpressionPairList;
import com.semantax.ast.node.list.NameTypeLitPairList;
import com.semantax.ast.node.list.NameTypePairList;
import com.semantax.ast.node.list.ParsableExpressionList;
import com.semantax.ast.node.literal.ArrayLit;
import com.semantax.ast.node.literal.BoolLit;
import com.semantax.ast.node.literal.FunctionLit;
import com.semantax.ast.node.literal.IntLit;
import com.semantax.ast.node.literal.RecordLit;
import com.semantax.ast.node.literal.StringLit;
import com.semantax.ast.node.literal.type.ArrayTypeLit;
import com.semantax.ast.node.literal.type.BoolTypeLit;
import com.semantax.ast.node.literal.type.FuncTypeLit;
import com.semantax.ast.node.literal.type.IntTypeLit;
import com.semantax.ast.node.literal.type.NameTypeLitPair;
import com.semantax.ast.node.literal.type.RecordTypeLit;
import com.semantax.ast.node.literal.type.StringTypeLit;
import com.semantax.ast.node.literal.type.TypeLit;
import com.semantax.ast.type.ArrayType;
import com.semantax.ast.type.FuncType;
import com.semantax.ast.type.IntType;
import com.semantax.ast.type.RecordType;
import com.semantax.ast.type.Type;
import com.semantax.ast.util.FilePos;
import com.semantax.error.ErrorType;
import com.semantax.logger.ErrorLogger;
import junit.framework.TestCase;

import java.util.Optional;
import java.util.Set;

import static com.semantax.ast.type.BoolType.BOOL_TYPE;
import static com.semantax.ast.type.IntType.INT_TYPE;
import static com.semantax.ast.type.StringType.STRING_TYPE;
import static com.semantax.ast.type.TypeType.TYPE_TYPE;
import static com.semantax.testutil.AstUtil.asList;
import static com.semantax.testutil.AstUtil.pair;
import static com.semantax.testutil.AstUtil.parsedTo;
import static com.semantax.testutil.AstUtil.withType;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DefaultTypeAnnotatorTest extends TestCase {

    private final TypeAssignabilityChecker typeAssignabilityChecker = new DefaultTypeAssignabilityChecker();
    private final RecordTypeUtil recordTypeUtil = new RecordTypeUtil();
    private final ErrorLogger errorLogger = mock(ErrorLogger .class);

    private final DefaultTypeAnnotator typeAnnotator =
            new DefaultTypeAnnotator(typeAssignabilityChecker, recordTypeUtil, errorLogger);

    public void test_int() {
        IntLit intLit = new IntLit(5);

        boolean result = typeAnnotator.visit(intLit);

        assertTrue(result);
        assertEquals(INT_TYPE, intLit.getType());
    }

    public void test_bool() {
        BoolLit boolLit = new BoolLit(true);

        boolean result = typeAnnotator.visit(boolLit);

        assertTrue(result);
        assertEquals(BOOL_TYPE, boolLit.getType());
    }

    public void test_string() {
        StringLit stringLit = new StringLit("hello");

        boolean result = typeAnnotator.visit(stringLit);

        assertTrue(result);
        assertEquals(STRING_TYPE, stringLit.getType());
    }

    public void test_array_positive() {
        // given
        ParsableExpression e0 = parsedTo(withType(new IntLit(5), INT_TYPE));
        ParsableExpression e1 = parsedTo(withType(new IntLit(10), INT_TYPE));

        // [5, 10]
        ArrayLit arrayLit = ArrayLit.builder()
                .values(asList(ParsableExpressionList.class, e0, e1))
                .build();

        // when
        boolean result = typeAnnotator.visit(arrayLit);

        // then
        assertTrue(result);
        assertTrue(arrayLit.getType() instanceof ArrayType);
        assertEquals(INT_TYPE, ((ArrayType) arrayLit.getType()).getSubType());
    }

    public void test_array_negative() {
        // given
        ParsableExpression e0 = parsedTo(withType(new IntLit(5), INT_TYPE));
        ParsableExpression e1 = parsedTo(withType(new BoolLit(true), BOOL_TYPE));

        FilePos filePos = FilePos.none();

        // [5, true]
        ArrayLit arrayLit = ArrayLit.builder()
                .values(asList(ParsableExpressionList.class, e0, e1))
                .buildWith(filePos);

        // when
        boolean result = typeAnnotator.visit(arrayLit);

        // then
        assertFalse(result);
        verify(errorLogger).error(eq(ErrorType.HETEROGENEOUS_ARRAY), eq(filePos), any(), anyVararg());
    }

    public void test_record_positive() {
        // given
        RecordLit recordLit = RecordLit.builder()
                .nameParsableExpressionPairs(asList(NameParsableExpressionPairList.class,
                        pair("a", parsedTo(withType(new IntLit(5), INT_TYPE))),
                        pair("b", parsedTo(withType(new BoolLit(true), BOOL_TYPE)))))
                .build();

        // when
        boolean result = typeAnnotator.visit(recordLit);

        // then
        assertTrue(result);
        assertTrue(recordLit.getType() instanceof RecordType);

        NameTypePairList nameTypePairs = ((RecordType) recordLit.getType()).getNameTypePairs();
        assertEquals(2, nameTypePairs.size());
        assertEquals("a", nameTypePairs.get(0).getName());
        assertEquals(INT_TYPE, nameTypePairs.get(0).getType());
        assertEquals("b", nameTypePairs.get(1).getName());
        assertEquals(BOOL_TYPE, nameTypePairs.get(1).getType());
    }

    public void test_record_negativeWithDuplicateName() {
        // given
        FilePos filePos = FilePos.none();

        RecordLit recordLit = RecordLit.builder()
                .nameParsableExpressionPairs(asList(NameParsableExpressionPairList.class,
                        pair("a", parsedTo(withType(new IntLit(5), INT_TYPE))),
                        pair("a", parsedTo(withType(new BoolLit(true), BOOL_TYPE)))))
                .buildWith(filePos);

        // when
        boolean result = typeAnnotator.visit(recordLit);

        // then
        assertFalse(result);
        verify(errorLogger).error(eq(ErrorType.DUPLICATE_RECORD_NAME), eq(filePos), any(), anyVararg());
    }

    public void test_func_withOutput() {
        // given
        FunctionLit functionLit = FunctionLit.builder()
                .input(RecordTypeLit.builder()
                        .nameTypeLitPairs(asList(NameTypeLitPairList.class,
                                pair("a", new IntTypeLit()),
                                pair("b", new BoolTypeLit())))
                        .build())
                .output(Optional.of(new IntTypeLit()))
                .build();

        // when
        boolean result = typeAnnotator.visit(functionLit);

        // then
        assertTrue(result);
        assertTrue(functionLit.getType() instanceof FuncType);

        Type inputType = ((FuncType) functionLit.getType()).getInputType();
        assertTrue(inputType instanceof RecordType);

        Optional<Type> outputType = ((FuncType) functionLit.getType()).getOutputType();
        assertTrue(outputType.isPresent());
        assertEquals(INT_TYPE, outputType.get());
    }

    public void test_func_withoutOutput() {
        // given
        FunctionLit functionLit = FunctionLit.builder()
                .input(RecordTypeLit.builder()
                        .nameTypeLitPairs(asList(NameTypeLitPairList.class,
                                pair("a", new IntTypeLit()),
                                pair("b", new BoolTypeLit())))
                        .build())
                .output(Optional.empty())
                .build();

        // when
        boolean result = typeAnnotator.visit(functionLit);

        // then
        assertTrue(result);
        assertTrue(functionLit.getType() instanceof FuncType);

        Type inputType = ((FuncType) functionLit.getType()).getInputType();
        assertTrue(inputType instanceof RecordType);

        Optional<Type> outputType = ((FuncType) functionLit.getType()).getOutputType();
        assertFalse(outputType.isPresent());
    }

    public void test_intTypeLit() {
        // given
        IntTypeLit intTypeLit = new IntTypeLit();

        // when
        boolean result = typeAnnotator.visit(intTypeLit);

        // then
        assertTrue(result);
        assertEquals(TYPE_TYPE, intTypeLit.getType());
        assertEquals(INT_TYPE, intTypeLit.getRepresentedType());
    }

    public void test_boolTypeLit() {
        // given
        BoolTypeLit boolTypeLit = new BoolTypeLit();

        // when
        boolean result = typeAnnotator.visit(boolTypeLit);

        // then
        assertTrue(result);
        assertEquals(TYPE_TYPE, boolTypeLit.getType());
        assertEquals(BOOL_TYPE, boolTypeLit.getRepresentedType());
    }

    public void test_stringTypeLit() {
        // given
        StringTypeLit stringTypeLit = new StringTypeLit();

        // when
        boolean result = typeAnnotator.visit(stringTypeLit);

        // then
        assertTrue(result);
        assertEquals(TYPE_TYPE, stringTypeLit.getType());
        assertEquals(STRING_TYPE, stringTypeLit.getRepresentedType());
    }

    public void test_arrayTypeLit() {
        // given
        ArrayTypeLit arrayTypeLit = ArrayTypeLit.builder()
                .subType(new IntTypeLit())
                .build();

        // when
        boolean result = typeAnnotator.visit(arrayTypeLit);

        // then
        assertTrue(result);
        assertEquals(TYPE_TYPE, arrayTypeLit.getType());
        assertTrue(arrayTypeLit.getRepresentedType() instanceof ArrayType);
        assertEquals(INT_TYPE, ((ArrayType) arrayTypeLit.getRepresentedType()).getSubType());
    }

    public void test_recordTypeLit() {
        // given
        RecordTypeLit recordTypeLit = RecordTypeLit.builder()
                .nameTypeLitPairs(asList(NameTypeLitPairList.class,
                        pair("a", new IntTypeLit()),
                        pair("b", new BoolTypeLit())))
                .build();

        // when
        boolean result = typeAnnotator.visit(recordTypeLit);

        // then
        assertTrue(result);
        assertEquals(TYPE_TYPE, recordTypeLit.getType());
        assertTrue(recordTypeLit.getRepresentedType() instanceof RecordType);
        NameTypePairList nameTypePairs = ((RecordType) recordTypeLit.getRepresentedType()).getNameTypePairs();
        assertEquals(2, nameTypePairs.size());
        assertEquals("a", nameTypePairs.get(0).getName());
        assertEquals(INT_TYPE, nameTypePairs.get(0).getType());
        assertEquals("b", nameTypePairs.get(1).getName());
        /**/
        assertEquals(BOOL_TYPE, nameTypePairs.get(1).getType());

    }

    public void test_funcTypeLit() {
        // given
        RecordTypeLit inputTypeLit = RecordTypeLit.builder()
                .nameTypeLitPairs(asList(NameTypeLitPairList.class,
                        pair("a", new IntTypeLit()),
                        pair("b", new BoolTypeLit())))
                .build();
        FuncTypeLit funcTypeLit = FuncTypeLit.builder()
                .inputType(inputTypeLit)
                .outputType(Optional.of(new IntTypeLit()))
                .build();

        // when
        boolean result = typeAnnotator.visit(funcTypeLit);

        // then
        assertTrue(result);
        assertEquals(TYPE_TYPE, funcTypeLit.getType());
        assertTrue(funcTypeLit.getInputType().getRepresentedType() instanceof RecordType);
        assertTrue(funcTypeLit.getOutputType().get().getRepresentedType() instanceof IntType);
    }
}