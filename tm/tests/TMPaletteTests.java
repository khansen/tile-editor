package tm.tests;

import tm.TMPalette;
import tm.colorcodecs.ColorCodec;
import tm.colorcodecs.DirectColorCodec;

final class TMPaletteTests {

    private TMPaletteTests() {}

    static void runAll() {
        testPaletteConstructionFromEntriesAndBytes();
        testPaletteEntryMutationAndResize();
        testPaletteSerializationToBytes();
    }

    private static void testPaletteConstructionFromEntriesAndBytes() {
        DirectColorCodec codec = new DirectColorCodec(
            "dc32", 32, 0x00FF0000, 0x0000FF00, 0x000000FF, 0xFF000000, "direct");
        int[] entries = new int[] {0x11223344, 0x55667788};
        TMPalette fromEntries = new TMPalette("p1", entries, codec, ColorCodec.LITTLE_ENDIAN, true);

        TestAssertions.assertEquals(2, fromEntries.getSize(), "Palette should preserve entry count");
        TestAssertions.assertEquals(0x11223344, fromEntries.getEntry(0), "Palette entry 0 should match constructor input");
        TestAssertions.assertEquals(codec.decode(0x55667788), fromEntries.getEntryRGB(1),
            "Palette entry RGB should decode constructor input");

        byte[] bytes = new byte[] {0x44, 0x33, 0x22, 0x11, (byte)0x88, 0x77, 0x66, 0x55};
        TMPalette fromBytes = new TMPalette("p2", bytes, 0, 2, codec, ColorCodec.LITTLE_ENDIAN, true);
        TestAssertions.assertEquals(0x11223344, fromBytes.getEntry(0), "Palette should decode bytes into native entries");
        TestAssertions.assertEquals(0x55667788, fromBytes.getEntry(1), "Palette should decode second byte entry");
    }

    private static void testPaletteEntryMutationAndResize() {
        DirectColorCodec codec = new DirectColorCodec(
            "dc32", 32, 0x00FF0000, 0x0000FF00, 0x000000FF, 0xFF000000, "direct");
        TMPalette palette = new TMPalette("p3", 2, codec, ColorCodec.LITTLE_ENDIAN);
        palette.setEntryRGB(0, 0x00010203);
        TestAssertions.assertEquals(0x00010203, palette.getEntryRGB(0), "setEntryRGB should set RGB value");

        palette.setSize(4);
        TestAssertions.assertEquals(4, palette.getSize(), "setSize should grow palette");
        TestAssertions.assertEquals(0x00010203, palette.getEntryRGB(0), "setSize should preserve existing entries");
    }

    private static void testPaletteSerializationToBytes() {
        DirectColorCodec codec = new DirectColorCodec(
            "dc32", 32, 0x00FF0000, 0x0000FF00, 0x000000FF, 0xFF000000, "direct");
        TMPalette palette = new TMPalette("p4", new int[] {0x01020304, 0x0A0B0C0D}, codec, ColorCodec.LITTLE_ENDIAN, true);
        byte[] bytes = palette.entriesToBytes();

        TestAssertions.assertArrayEquals(
            new byte[] {0x04, 0x03, 0x02, 0x01, 0x0D, 0x0C, 0x0B, 0x0A},
            bytes,
            "entriesToBytes should serialize entries in selected endianness");
    }
}
