package com.semantax.logger;

import com.semantax.ast.util.FilePos;
import com.semantax.error.ErrorType;
import com.semantax.module.UtilModule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Singleton
public class ErrorLogger {

    private final ArrayList<Error> errors = new ArrayList<>();

    private final PrintStream out;

    @Inject
    public ErrorLogger(@Named(UtilModule.ERROR_LOGGING_PRINT_STREAM) PrintStream out) {
        this.out = out;
    }


    public void error(ErrorType errorType, FilePos filePos, String message, Object... args) {
        synchronized (errors) {
            errors.add(Error.builder()
                        .message(String.format(message, args))
                        .position(filePos)
                        .build());
        }
    }

    public void flush() {
        List<Error> errorsToPrint;
        synchronized (errors) {
            errorsToPrint = new ArrayList<>(errors);
            errors.clear();
        }

        errorsToPrint.sort(Comparator.comparing(error -> error.position));
        for (Error error : errorsToPrint) {
            out.printf("Error at %s:%n\t%s%n", error.position, error.message);
        }
    }

    @Builder
    private static class Error {
        private final String message;
        private final FilePos position;
    }

}
