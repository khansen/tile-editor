package tm.tests;

import tm.threads.FileLoaderThread;

import java.io.File;
import java.nio.file.Files;

final class FileLoaderThreadTests {

    private FileLoaderThreadTests() {}

    static void runAll() throws Exception {
        testFileLoaderHandlesEmptyFiles();
        testFileLoaderReadsAllBytes();
    }

    private static void testFileLoaderHandlesEmptyFiles() throws Exception {
        File temp = File.createTempFile("tm-loader-empty", ".bin");
        temp.deleteOnExit();
        Files.write(temp.toPath(), new byte[0]);

        FileLoaderThread thread = new FileLoaderThread(temp);
        TestAssertions.assertEquals(100, thread.getPercentageCompleted(),
            "Empty file should be 100% complete immediately");

        thread.start();
        thread.join(5000);

        TestAssertions.assertFalse(thread.isAlive(), "FileLoaderThread should complete");
        TestAssertions.assertTrue(thread.getFailure() == null, "FileLoaderThread should not fail for empty file");
        TestAssertions.assertEquals(0, thread.getContents().length, "Empty file should load as empty buffer");
    }

    private static void testFileLoaderReadsAllBytes() throws Exception {
        File temp = File.createTempFile("tm-loader-data", ".bin");
        temp.deleteOnExit();
        byte[] expected = new byte[70000];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = (byte)(i & 0xFF);
        }
        Files.write(temp.toPath(), expected);

        FileLoaderThread thread = new FileLoaderThread(temp);
        thread.start();
        thread.join(5000);

        TestAssertions.assertFalse(thread.isAlive(), "FileLoaderThread should complete");
        TestAssertions.assertTrue(thread.getFailure() == null, "FileLoaderThread should not fail");
        TestAssertions.assertEquals(100, thread.getPercentageCompleted(), "FileLoaderThread should report completion");
        TestAssertions.assertArrayEquals(expected, thread.getContents(), "FileLoaderThread should read all bytes");
    }
}
