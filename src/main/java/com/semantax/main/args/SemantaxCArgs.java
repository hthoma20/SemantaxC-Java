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
    private List<String> inputFiles;
    @lombok.Builder.Default
    private Optional<String> outputDir = Optional.empty();
}
