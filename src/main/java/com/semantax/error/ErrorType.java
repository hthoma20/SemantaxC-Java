package com.semantax.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ErrorType {
    // Meant to be used in tests only
    TEST("TEST-0"),

    INVALID_ARGUMENTS("ARG-0"),

    INVALID_FILE("FILE-0"),
    MISSING_INPUT_FILE("FILE-1"),
    TOO_MANY_INPUT_FILES("FILE-2"),

    PROGRAM_PARSE_ERROR("PARSE1-0"),

    MISSING_MAIN_MODULE("PARSE2-0"),
    UNPARSABLE_PHRASE("PARSE2-1"),
    ILLEGAL_DECL("PARSE2-2"),
    MULTIPLE_MAIN_MODULES("PARSE2-3"),

    HETEROGENEOUS_ARRAY("TYPEA-0"),
    DUPLICATE_RECORD_NAME("TYPEA-1"),
    DUPLICATE_RECORD_TYPE_NAME("TYPEA-2"),

    ILLEGAL_BIND("SEM-0");

    @Getter
    private final String id;

}
