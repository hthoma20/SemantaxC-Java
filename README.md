Semantax-Java is a Java implementation of a compiler for the 
Semantax programming language.


#Testing

Tests for phase 1 of the parser are found in test/com.semantax.parser.generated.SymantaxParserTest.java.
This class contains tests for positive an negative cases. For positive cases,
a snapshot approach is used (inspired by jest). In the `test_data` directory,
there is a collection of Semantax programs that are syntactically correct,
at least up through phase 1 of the parser.

The SemantaxParserTest.test_snapshot method tests each file in
its parameterized list of files, by doing the following:
1. Create a parser for the file
2. Parse the file with the Program production
3. Create a String representation of the resulting AST
4. 
   * Compare this to the existing snapshot and raise an assertion
   error if there is a mismatch.
   * If the snapshot does not exist write the snapshot

This ensures that changes to the parser which affect the AST will
be reviewed manually. If a snapshot does fail, the diff should be
manually reviewed to confirm that the change is expected and correct.
If it is, the existing snapshot should be deleted, and the test
re-run so that new snapshot is written.
When adding a new test file, no assertion will fail, but the snapshot should
be reviewed for correctness.
