package com.semantax.ast.factory;

import com.semantax.ast.node.literal.EmptyRecord;
import com.semantax.ast.util.FilePos;
import com.semantax.exception.UnexpectedTokenException;
import com.semantax.parser.generated.Token;

import static com.semantax.parser.generated.SymantaxParserConstants.*;

public class EmptyRecordFactory {

    public static EmptyRecord fromToken(Token token) {

        if (token.kind != L_PAREN) {
            throw new UnexpectedTokenException(token);
        }

        FilePos filePos = new FilePos(token.beginLine, token.beginColumn);

        EmptyRecord emptyRecord = new EmptyRecord();
        emptyRecord.setFilePos(filePos);
        return emptyRecord;
    }
}
