package tm.tests;

import tm.colorcodecs.ColorCodec;
import tm.colorcodecs.DirectColorCodec;
import tm.colorcodecs.IndexedColorCodec;

final class ColorCodecTests {

    private ColorCodecTests() {}

    static void runAll() {
        testDirectColorCodecEncodeDecodeRoundTrip();
        testColorCodecByteOrderConversions();
        testIndexedColorCodecDecodeAndNearestEncode();
    }

    private static void testDirectColorCodecEncodeDecodeRoundTrip() {
        DirectColorCodec codec = new DirectColorCodec(
            "dc32", 32, 0x00FF0000, 0x0000FF00, 0x000000FF, 0xFF000000, "direct");
        int[] argbValues = new int[] {
            0x00000000, 0xFFFFFFFF, 0x11223344, 0x80ABCDEF, 0x7F010203
        };
        for (int argb : argbValues) {
            int encoded = codec.encode(argb);
            int decoded = codec.decode(encoded);
            TestAssertions.assertEquals(argb, decoded, "DirectColorCodec should round-trip ARGB values");
        }
    }

    private static void testColorCodecByteOrderConversions() {
        DirectColorCodec codec = new DirectColorCodec(
            "dc32", 32, 0x00FF0000, 0x0000FF00, 0x000000FF, 0xFF000000, "direct");
        byte[] bytes = new byte[4];

        codec.setEndianness(ColorCodec.LITTLE_ENDIAN);
        codec.toBytes(0x11223344, bytes, 0);
        TestAssertions.assertArrayEquals(
            new byte[] {0x44, 0x33, 0x22, 0x11},
            bytes,
            "toBytes should write little-endian order");
        TestAssertions.assertEquals(
            0x11223344,
            codec.fromBytes(bytes, 0),
            "fromBytes should read little-endian order");

        codec.setEndianness(ColorCodec.BIG_ENDIAN);
        codec.toBytes(0x11223344, bytes, 0);
        TestAssertions.assertArrayEquals(
            new byte[] {0x11, 0x22, 0x33, 0x44},
            bytes,
            "toBytes should write big-endian order");
        TestAssertions.assertEquals(
            0x11223344,
            codec.fromBytes(bytes, 0),
            "fromBytes should read big-endian order");
    }

    private static void testIndexedColorCodecDecodeAndNearestEncode() {
        int[] table = new int[] {0x000000, 0xFF0000, 0x00FF00, 0x0000FF};
        IndexedColorCodec codec = new IndexedColorCodec("idx2", 2, table, "indexed");
        TestAssertions.assertEquals(0x00FF00, codec.decode(2), "decode should return table entry");
        TestAssertions.assertEquals(0, codec.decode(99), "decode should return 0 for out-of-range indices");
        TestAssertions.assertEquals(1, codec.encode(0xEE2200), "encode should choose nearest color table entry");
    }
}
