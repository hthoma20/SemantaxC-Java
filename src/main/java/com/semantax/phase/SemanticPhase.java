package com.semantax.phase;

import com.semantax.ast.node.Expression;
import com.semantax.ast.node.Program;
import com.semantax.ast.node.VariableReference;
import com.semantax.ast.node.progcall.BindProgCall;
import com.semantax.ast.node.progcall.ProgCall;
import com.semantax.ast.node.progcall.ReturnProgCall;
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

        /**
         * Check the number of subexpressions and log an error if the expected number
         * doesn't match
         * @return true if the given progcall has the given expected subexpressions
         */
        private boolean checkSubexpressionCount(ProgCall progCall, int expectedSubexpressions, ErrorType errorType) {
            int subExpressions = progCall.getSubExpressions().size();
            if (subExpressions != expectedSubexpressions) {
                error(errorType, progCall.getFilePos(), "%s expects %d arguments, %d were given",
                        progCall.getName(), expectedSubexpressions, subExpressions);
                return false;
            }
            return true;
        }

        @Override
        public Void visit(BindProgCall bindProgCall) {
            super.visit(bindProgCall);

            if (!checkSubexpressionCount(bindProgCall, 2, ErrorType.ILLEGAL_BIND)) {
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

        @Override
        public Void visit(ReturnProgCall returnProgCall) {
            super.visit(returnProgCall);

            if (!checkSubexpressionCount(returnProgCall, 1, ErrorType.ILLEGAL_BIND)) {
                return null;
            }

            Optional<Expression> returnExpression = getSubexpression(returnProgCall, 0, Expression.class);
            if (!returnExpression.isPresent()) {
                error(ErrorType.ILLEGAL_RETURN, returnExpression.get().getFilePos(), "Missing return expression");
                return null;
            }

            returnProgCall.setReturnExpression(returnExpression.get());

            return null;
        }
    }


}
