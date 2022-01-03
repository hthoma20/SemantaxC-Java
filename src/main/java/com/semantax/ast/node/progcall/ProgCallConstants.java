package com.semantax.ast.node.progcall;

import com.semantax.ast.type.IntType;
import com.semantax.ast.type.StringType;

import java.util.Arrays;
import java.util.Optional;

public final class ProgCallConstants {
    public static final String DECL = "decl";
    public static final String BIND = "bind";

    public static final String PRINT_INT = "printint";
    public static final String PRINT_STRING = "printstring";
    public static final String ADD_INT = "addint";
    public static final String INVOKE_FUN = "invokefun";

    public static final ProgCall.Builder PRINT_INT_BUILDER = DynamicProgcall.builder()
            .parameterTypes(Arrays.asList(IntType.INT_TYPE))
            .returnType(Optional.empty());

    public static final ProgCall.Builder PRINT_STRING_BUILDER = DynamicProgcall.builder()
            .parameterTypes(Arrays.asList(StringType.STRING_TYPE))
            .returnType(Optional.empty());

    public static final ProgCall.Builder ADD_INT_BUILDER = DynamicProgcall.builder()
            .parameterTypes(Arrays.asList(IntType.INT_TYPE, IntType.INT_TYPE))
            .returnType(Optional.of(IntType.INT_TYPE));

    public static final ProgCall.Builder INVOKE_FUN_BUILDER = DynamicProgcall.builder();
}
