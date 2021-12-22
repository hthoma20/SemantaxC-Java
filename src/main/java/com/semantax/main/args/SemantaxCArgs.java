package com.semantax.main.args;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;
import java.util.Optional;

@Value
@Builder(builderClassName = "Builder")
public class SemantaxCArgs {
    @Singular
    private final List<String> inputFiles;
    private final String outputFile;
    private final Optional<String> astFile;
    private final boolean enableBreadCrumbs;
}
