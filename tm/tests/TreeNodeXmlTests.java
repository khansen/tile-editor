package tm.tests;

import tm.TMPalette;
import tm.colorcodecs.ColorCodec;
import tm.colorcodecs.DirectColorCodec;
import tm.tilecodecs.DirectColorTileCodec;
import tm.tilecodecs.TileCodec;
import tm.treenodes.BookmarkItemNode;
import tm.treenodes.FolderNode;
import tm.treenodes.PaletteItemNode;

final class TreeNodeXmlTests {

    private TreeNodeXmlTests() {}

    static void runAll() {
        testFolderXmlEscaping();
        testPaletteXmlEscaping();
        testBookmarkXmlEscaping();
    }

    private static void testFolderXmlEscaping() {
        FolderNode folder = new FolderNode("A<&\"'>");
        String xml = folder.toXML();
        TestAssertions.assertTrue(
            xml.contains("<name>A&lt;&amp;&quot;&apos;&gt;</name>"),
            "FolderNode should escape name in XML");
    }

    private static void testPaletteXmlEscaping() {
        DirectColorCodec codec = new DirectColorCodec(
            "dc16", 16, 0x00007C00, 0x000003E0, 0x0000001F, 0x00000000, "codec");
        TMPalette palette = new TMPalette("pal1", 4, codec, ColorCodec.LITTLE_ENDIAN);
        PaletteItemNode node = new PaletteItemNode(palette, "P<&\"'>");
        String xml = node.toXML();
        TestAssertions.assertTrue(
            xml.contains("<description>P&lt;&amp;&quot;&apos;&gt;</description>"),
            "PaletteItemNode should escape description in XML");
    }

    private static void testBookmarkXmlEscaping() {
        DirectColorTileCodec codec = new DirectColorTileCodec(
            "dc32", 32, 0x00FF0000, 0x0000FF00, 0x000000FF, 0xFF000000, "codec");
        BookmarkItemNode bookmark = new BookmarkItemNode(
            0, 1, 1, 1, 1, false, false, TileCodec.MODE_1D, 0, codec, "B<&\"'>");
        String xml = bookmark.toXML();
        TestAssertions.assertTrue(
            xml.contains("<description>B&lt;&amp;&quot;&apos;&gt;</description>"),
            "BookmarkItemNode should escape description in XML");
    }
}
