package com.semantax.phase.grammar;

import com.semantax.parser.generated.ParseException;
import com.semantax.parser.generated.SemantaxParser;
import com.semantax.parser.generated.Token;
import lombok.SneakyThrows;

import java.util.Iterator;
import java.util.Stack;

import static com.semantax.parser.generated.SemantaxParserConstants.COMMA;
import static com.semantax.parser.generated.SemantaxParserConstants.L_BRACKET;
import static com.semantax.parser.generated.SemantaxParserConstants.R_BRACE;
import static com.semantax.parser.generated.SemantaxParserConstants.R_BRACKET;
import static com.semantax.parser.generated.SemantaxParserConstants.R_PAREN;
import static com.semantax.parser.generated.SemantaxParserConstants.SEMI_COLON;

public class LookaheadUtil {
    private final SemantaxParser parser;

    private final Stack<BracketConsumer> bracketStack = new Stack<>();

    public LookaheadUtil(SemantaxParser parser) {
        this.parser = parser;
    }

    /**
     * When parsing a Phrase, determine if the next token should be taken
     * as a PhraseElement, or if this token ends the phrase.
     * @return whether the next token should end a Phrase
     */
    @SneakyThrows
    public boolean isEndOfPhrase() {
        Token nextToken = parser.getToken(1);

        switch (nextToken.kind) {
        case R_PAREN:
        case R_BRACE:
        case SEMI_COLON:
        case COMMA:
            return true;
        case R_BRACKET:
            break;
        default:
            return false;
        }

        // if we reach here, we are looking at an R_BRACKET and we must decide
        // whether it should end the phrase, or be taken as a symbol.
        // this only depends on what the previous L_BRACKET was
        switch (bracketStack.peek()) {
            case SYMBOL:
                return false;
            case ARRAY_LIT:
                return true;
        }

        throw new ParseException("Unexpected bracket");
    }

    /**
     * When parsing a PhraseElement, determine if the next token should be
     * taken as an ArrayLit
     * @return whether the next token begins an ArrayLit
     */
    @SneakyThrows
    public boolean isArrayLit() {
        Token nextToken = parser.getToken(1);

        if (nextToken.kind != L_BRACKET) {
            return false;
        }

        // an open bracket belongs to a list if the next token is `]` (i.e `[]`), or
        // if a comma comes before the next close bracket
        return parser.getToken(2).kind == R_BRACKET || startsCommaList();
    }

    /**
     * lookahead to the next tokens and determine which comes first, a COMMA or
     * an R_BRACKET
     * @return true if there is a COMMA before an R_BRACKET, false if R_BRACKET before comma
     */
    private boolean startsCommaList() throws ParseException {
        int unclosedBrackets = 0;

        Iterator<Token> tokenIterator = parser.iterateTokens();
        while (tokenIterator.hasNext()) {
            Token token = tokenIterator.next();

            switch (token.kind) {
                case L_BRACKET:
                    unclosedBrackets++;
                    break;
                case R_BRACKET:
                    unclosedBrackets--;
                    break;
            }

            // if we are looking at a comma, and we are at the "top level bracket"
            // this starts a list with a comma
            if (token.kind == COMMA && unclosedBrackets == 1) {
                return true;
            }

            // if we closed the first bracket without seeing a comma,
            // this does not start a list with a comma
            if (unclosedBrackets == 0) {
                return false;
            }
        }

        throw new ParseException("Cannot find close paren");
    }


    /**
     * Mark that a L_BRACKET was consumed
     * @param consumer the type of production the token was consumed for
     */
    public void consumedLBracket(BracketConsumer consumer) {
        bracketStack.push(consumer);
    }

    /**
     * Mark that an R_BRACKET was consumed
     */
    @SneakyThrows
    public void consumedRBracket(BracketConsumer consumer) throws ParseException {
        BracketConsumer openBracket = bracketStack.pop();
        if (openBracket != consumer) {
            throw new ParseException("Unmatched bracket");
        }
    }

    /**
     * The parse rules that can consume an open bracket
     */
    public enum BracketConsumer {
        SYMBOL, ARRAY_LIT, PATTERN
    }
}
