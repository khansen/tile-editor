package tm.tests;

import tm.TMBitmapExporter;
import tm.TMBitmapImporter;

import java.io.File;

final class BitmapUtilityTests {

    private BitmapUtilityTests() {}

    static void runAll() {
        testBitmapExtensionHelpers();
    }

    private static void testBitmapExtensionHelpers() {
        TestAssertions.assertEquals("png", TMBitmapImporter.getExtension(new File("tiles.PNG")),
            "Importer should normalize extension to lowercase");
        TestAssertions.assertEquals("jpg", TMBitmapExporter.getExtension(new File("image.jpg")),
            "Exporter should parse jpg extension");
        TestAssertions.assertEquals("", TMBitmapImporter.getExtension(new File("no_extension")),
            "Importer should return empty extension when none exists");
        TestAssertions.assertEquals("", TMBitmapExporter.getExtension(new File(".hiddenfile")),
            "Exporter should return empty extension for dot-prefixed filenames without ext");
    }
}
