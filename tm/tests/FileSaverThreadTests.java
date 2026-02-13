package tm.tests;

import tm.threads.FileSaverThread;

import java.io.File;
import java.nio.file.Files;

final class FileSaverThreadTests {

    private FileSaverThreadTests() {}

    static void runAll() throws Exception {
        testFileSaverTruncatesOutput();
        testFileSaverHandlesEmptyBuffers();
    }

    private static void testFileSaverTruncatesOutput() throws Exception {
        File temp = File.createTempFile("tm-saver-truncate", ".bin");
        temp.deleteOnExit();

        Files.write(temp.toPath(), new byte[] {1, 2, 3, 4, 5, 6, 7, 8});
        byte[] replacement = new byte[] {9, 10, 11};

        FileSaverThread thread = new FileSaverThread(replacement, temp);
        thread.start();
        thread.join(5000);

        TestAssertions.assertFalse(thread.isAlive(), "FileSaverThread should complete");
        TestAssertions.assertTrue(thread.getFailure() == null, "FileSaverThread should not fail");
        TestAssertions.assertEquals(100, thread.getPercentageCompleted(), "FileSaverThread should report completion");

        byte[] saved = Files.readAllBytes(temp.toPath());
        TestAssertions.assertArrayEquals(replacement, saved, "FileSaverThread should truncate stale trailing bytes");
    }

    private static void testFileSaverHandlesEmptyBuffers() throws Exception {
        File temp = File.createTempFile("tm-saver-empty", ".bin");
        temp.deleteOnExit();
        Files.write(temp.toPath(), new byte[] {1, 2, 3});

        FileSaverThread thread = new FileSaverThread(new byte[0], temp);
        TestAssertions.assertEquals(100, thread.getPercentageCompleted(),
            "Empty save buffer should be 100% complete immediately");

        thread.start();
        thread.join(5000);

        TestAssertions.assertFalse(thread.isAlive(), "FileSaverThread should complete");
        TestAssertions.assertTrue(thread.getFailure() == null, "FileSaverThread should not fail for empty save");
        TestAssertions.assertEquals(0L, temp.length(), "Saving empty buffer should truncate file to zero bytes");
    }
}
