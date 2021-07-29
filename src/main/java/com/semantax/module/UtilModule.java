package com.semantax.module;

import dagger.Module;
import dagger.Provides;

import javax.inject.Named;
import java.io.PrintStream;

@Module
public class UtilModule {
    public static final String ERROR_LOGGING_PRINT_STREAM = "errorLoggingPrintStream";

    @Provides
    @Named(ERROR_LOGGING_PRINT_STREAM)
    public PrintStream errorLoggingPrintStream() {
        return System.err;
    }
}
