package com.semantax.logger;

import com.semantax.ast.util.FilePos;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
public class ErrorLogger {

    private final ArrayList<Error> errors = new ArrayList<>();

    private final PrintStream out;

    public void error(String message, FilePos filePos) {
        synchronized (errors) {
            errors.add(Error.builder()
                        .message(message)
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
