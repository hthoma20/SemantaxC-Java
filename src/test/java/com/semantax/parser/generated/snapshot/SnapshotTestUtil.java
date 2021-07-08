package com.semantax.parser.generated.snapshot;

import com.semantax.ast.node.AstNode;
import com.semantax.ast.visitor.AstToStringVisitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import lombok.AllArgsConstructor;

import static org.junit.Assert.assertEquals;

@AllArgsConstructor
public class SnapshotTestUtil {

    private String testDataRoot;

    public void assertMatchesSnapshot(String snapshotName, AstNode node) {

        String nodeString = new AstToStringVisitor().visit(node);

        if (!snapshotExists(snapshotName)) {
            System.out.printf("Snapshot [%s] doesn't exist, saving it%n", snapshotName);
            writeSnapshot(snapshotName, nodeString);
            return;
        }

        String snapshotString = readSnapshot(snapshotName);

        assertEquals(String.format("Snapshot mismatch for: %s", snapshotName), snapshotString, nodeString);
    }

    private boolean snapshotExists(String snapshotName) {
        return new File(getFileName(snapshotName)).exists();
    }

    private String readSnapshot(String snapshotName) {
        try {
            return new String(Files.readAllBytes(Paths.get(getFileName(snapshotName))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeSnapshot(String snapshotName, String snapshot) {
        File file = new File(getFileName(snapshotName));
        try (FileOutputStream out = new FileOutputStream(file)) {
            out.write(snapshot.getBytes());
        }
        catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    private String getFileName(String snapshotName) {
        return String.format("%s/snapshots/%s.snap", testDataRoot, snapshotName);
    }

}
