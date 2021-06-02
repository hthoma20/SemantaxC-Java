package com.symantax.parser.generated.snapshot;

import com.symantax.ast.node.AstNode;
import com.symantax.ast.visitor.AstToStringVisitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;

public class SnapshotTestUtil {

    public static void assertMatchesSnapshot(String snapshotName, AstNode node) {

        String nodeString = new AstToStringVisitor().visit(node);

        if (!snapshotExists(snapshotName)) {
            System.out.printf("Snapshot [%s] doesn't exist, saving it%n", snapshotName);
            writeSnapshot(snapshotName, nodeString);
            return;
        }

        String snapshotString = readSnapshot(snapshotName);

        Assertions.assertEquals(snapshotString, nodeString);
    }

    private static boolean snapshotExists(String snapshotName) {
        return new File(getFileName(snapshotName)).exists();
    }

    private static String readSnapshot(String snapshotName) {
        try {
            return new String(Files.readAllBytes(Paths.get(getFileName(snapshotName))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeSnapshot(String snapshotName, String snapshot) {
        File file = new File(getFileName(snapshotName));
        try (FileOutputStream out = new FileOutputStream(file)) {
            out.write(snapshot.getBytes());
        }
        catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    private static String getFileName(String snapshotName) {
        return String.format("./test_data/snapshots/%s.snap", snapshotName);
    }

}
