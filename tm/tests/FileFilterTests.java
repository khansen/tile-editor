package tm.tests;

import tm.fileselection.TMFileFilter;
import tm.fileselection.TMPaletteFileFilter;

import java.io.File;
import java.nio.file.Files;

final class FileFilterTests {

    private FileFilterTests() {}

    static void runAll() throws Exception {
        testTMFileFilterMatchingAndMetadata();
        testTMFileFilterWildcardMatching();
        testTMPaletteFileFilterProperties();
    }

    private static void testTMFileFilterMatchingAndMetadata() throws Exception {
        TMFileFilter filter = new TMFileFilter("bin,rom", "Binary files");
        TestAssertions.assertEquals("bin", filter.getDefaultExtension(), "Default extension should be first entry");
        TestAssertions.assertEquals("Binary files", filter.getDescription(), "Description should match constructor value");
        TestAssertions.assertEquals("bin,rom", filter.getExtlist(), "Extlist should match constructor value");

        File binFile = File.createTempFile("tm-filter", ".bin");
        File txtFile = File.createTempFile("tm-filter", ".txt");
        binFile.deleteOnExit();
        txtFile.deleteOnExit();

        TestAssertions.assertTrue(filter.accept(binFile), "Filter should accept registered extension");
        TestAssertions.assertFalse(filter.accept(txtFile), "Filter should reject unregistered extension");

        File dir = Files.createTempDirectory("tm-filter-dir").toFile();
        dir.deleteOnExit();
        TestAssertions.assertTrue(filter.accept(dir), "Filter should always accept directories");
    }

    private static void testTMFileFilterWildcardMatching() throws Exception {
        TMFileFilter filter = new TMFileFilter("?bc", "Wildcard");
        File abc = File.createTempFile("tm-filter", ".abc");
        File zbc = File.createTempFile("tm-filter", ".zbc");
        File dbcx = File.createTempFile("tm-filter", ".dbcx");
        abc.deleteOnExit();
        zbc.deleteOnExit();
        dbcx.deleteOnExit();

        TestAssertions.assertTrue(filter.accept(abc), "Wildcard should match .abc");
        TestAssertions.assertTrue(filter.accept(zbc), "Wildcard should match .zbc");
        TestAssertions.assertFalse(filter.accept(dbcx), "Wildcard should not match different lengths");
        TestAssertions.assertEquals("png", TMFileFilter.getExtension(new File("sprite.PNG")),
            "getExtension should normalize to lowercase");
    }

    private static void testTMPaletteFileFilterProperties() {
        TMPaletteFileFilter filter = new TMPaletteFileFilter("pal", "Palette", "rgb565", 16, 32, 1);
        TestAssertions.assertEquals("rgb565", filter.getCodecID(), "Palette filter should expose codec id");
        TestAssertions.assertEquals(16, filter.getSize(), "Palette filter should expose palette size");
        TestAssertions.assertEquals(32, filter.getOffset(), "Palette filter should expose offset");
        TestAssertions.assertEquals(1, filter.getEndianness(), "Palette filter should expose endianness");
    }
}
