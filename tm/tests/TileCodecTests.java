package tm.tests;

import tm.tilecodecs.CompositeTileCodec;
import tm.tilecodecs.DirectColorTileCodec;
import tm.tilecodecs.LinearTileCodec;
import tm.tilecodecs.PlanarTileCodec;
import tm.tilecodecs.TileCodec;
import tm.tilecodecs._3BPPLinearTileCodec;

final class TileCodecTests {

    private TileCodecTests() {}

    static void runAll() {
        testLinearTileCodecRoundTrip();
        testPlanarTileCodecRoundTrip();
        testDirectColorTileCodecRoundTrip();
        testThreeBppLinearTileCodecRoundTrip();
        testCompositeTileCodecRoundTrip();
    }

    private static void testLinearTileCodecRoundTrip() {
        LinearTileCodec codec = new LinearTileCodec("l4", 4, LinearTileCodec.IN_ORDER, "linear4");
        int[] pixels = buildPattern(64, 16);
        byte[] bits = new byte[codec.getTileSize()];
        codec.encode(pixels.clone(), bits, 0, 0);
        int[] decoded = codec.decode(bits, 0, 0);
        assertTilePixelsEqual(pixels, decoded, "LinearTileCodec should round-trip tile pixels");
        TestAssertions.assertEquals(32, codec.getTileSize(), "4bpp tile size should be 32 bytes");
        TestAssertions.assertEquals(4, codec.getBytesPerRow(), "4bpp tile row should be 4 bytes");
    }

    private static void testPlanarTileCodecRoundTrip() {
        PlanarTileCodec codec = new PlanarTileCodec("p2", new int[] {0, 1}, "planar2");
        int[] pixels = buildPattern(64, 4);
        byte[] bits = new byte[codec.getTileSize()];
        codec.encode(pixels.clone(), bits, 0, 0);
        int[] decoded = codec.decode(bits, 0, 0);
        assertTilePixelsEqual(pixels, decoded, "PlanarTileCodec should round-trip tile pixels");
    }

    private static void testDirectColorTileCodecRoundTrip() {
        DirectColorTileCodec codec = new DirectColorTileCodec(
            "dc32", 32, 0x00FF0000, 0x0000FF00, 0x000000FF, 0xFF000000, "direct32");
        int[] pixels = new int[64];
        for (int i = 0; i < pixels.length; i++) {
            int c = i * 3;
            pixels[i] = 0xFF000000 | ((c & 0xFF) << 16) | (((c + 32) & 0xFF) << 8) | ((c + 64) & 0xFF);
        }
        byte[] bits = new byte[codec.getTileSize()];
        codec.encode(pixels.clone(), bits, 0, 0);
        int[] decoded = codec.decode(bits, 0, 0);
        assertTilePixelsEqual(pixels, decoded, "DirectColorTileCodec should round-trip tile pixels");
    }

    private static void testThreeBppLinearTileCodecRoundTrip() {
        _3BPPLinearTileCodec codec = new _3BPPLinearTileCodec();
        int[] pixels = buildPattern(64, 8);
        byte[] bits = new byte[codec.getTileSize()];
        codec.encode(pixels.clone(), bits, 0, 0);
        int[] decoded = codec.decode(bits, 0, 0);
        assertTilePixelsEqual(pixels, decoded, "_3BPPLinearTileCodec should round-trip tile pixels");
    }

    private static void testCompositeTileCodecRoundTrip() {
        TileCodec low = new LinearTileCodec("l2", 2, LinearTileCodec.IN_ORDER, "low2");
        TileCodec hi = new LinearTileCodec("l1", 1, LinearTileCodec.IN_ORDER, "hi1");
        CompositeTileCodec codec = new CompositeTileCodec("c3", 3, new TileCodec[] {low, hi}, "composite3");

        int[] pixels = buildPattern(64, 8);
        byte[] bits = new byte[low.getTileSize() + hi.getTileSize()];
        codec.encode(pixels.clone(), bits, 0, 0);
        int[] decoded = codec.decode(bits, 0, 0);
        assertTilePixelsEqual(pixels, decoded, "CompositeTileCodec should round-trip tile pixels");
    }

    private static int[] buildPattern(int count, int modulus) {
        int[] data = new int[count];
        for (int i = 0; i < count; i++) {
            data[i] = i % modulus;
        }
        return data;
    }

    private static void assertTilePixelsEqual(int[] expected, int[] actual, String message) {
        TestAssertions.assertEquals(expected.length, actual.length, message + " (length mismatch)");
        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != actual[i]) {
                throw new AssertionError(message + " (mismatch at index " + i + ")");
            }
        }
    }
}
