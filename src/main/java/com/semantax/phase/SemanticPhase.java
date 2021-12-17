package com.semantax.phase;

import com.semantax.ast.node.Expression;
import com.semantax.ast.node.Program;
import com.semantax.ast.node.VariableReference;
import com.semantax.ast.node.progcall.BindProgCall;
import com.semantax.ast.node.progcall.ProgCall;
import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.TraversalVisitor;
import com.semantax.error.ErrorType;
import com.semantax.logger.ErrorLogger;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Perform semantic checks on the given program.
 */
public class SemanticPhase implements Phase<Program, Program> {

    private final ErrorLogger errorLogger;

    @Inject
    public SemanticPhase(ErrorLogger errorLogger) {
        this.errorLogger = errorLogger;
    }

    @Override
    public Optional<Program> process(Program input) {
        SemanticVisitor visitor = new SemanticVisitor();
        input.accept(visitor);

        if (visitor.hasError) {
            return Optional.empty();
        }
        return Optional.of(input);
    }

    private <T extends Expression> Optional<T> getSubexpression(ProgCall progCall, int index, Class<T> clazz) {
        Expression subexpression = progCall.getSubExpressions().get(index).getExpression();

        if (clazz.isInstance(subexpression)) {
            return Optional.of(clazz.cast(subexpression));
        };

        return Optional.empty();
    }

    private class SemanticVisitor extends TraversalVisitor<Void> {

        private boolean hasError = false;

        private void error(ErrorType errorType, FilePos filePos, String message, Object... args) {
            errorLogger.error(errorType, filePos, message, args);
            hasError = true;
        }

        @Override
        public Void visit(BindProgCall bindProgCall) {

            if (bindProgCall.getSubExpressions().size() != 2) {
                error(ErrorType.ILLEGAL_BIND, bindProgCall.getFilePos(),
                        "%s expects a name and value", bindProgCall.getName());
                return null;
            }

            Optional<VariableReference> variableReference =
                    getSubexpression(bindProgCall, 0, VariableReference.class);
            if (!variableReference.isPresent()) {
                error(ErrorType.ILLEGAL_BIND, bindProgCall.getFilePos(),
                        "The first argument to %s must be a name", bindProgCall.getName());
                return null;
            }

            Optional<Expression> value =
                    getSubexpression(bindProgCall, 1, Expression.class);
            if (!value.isPresent()) {
                error(ErrorType.ILLEGAL_BIND, bindProgCall.getFilePos(),
                        "The second argument to %s must be an expression", bindProgCall.getName());
                return null;
            }

            bindProgCall.setVariableReference(variableReference.get());
            bindProgCall.setValue(value.get());

            return null;
        }
    }
}
