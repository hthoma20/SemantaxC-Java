package com.symantax.ast.factory;

import com.symantax.ast.node.literal.EmptyRecord;
import com.symantax.ast.util.FilePos;
import com.symantax.exception.UnexpectedTokenException;
import com.symantax.parser.generated.Token;

import static com.symantax.parser.generated.SymantaxParserConstants.*;

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
