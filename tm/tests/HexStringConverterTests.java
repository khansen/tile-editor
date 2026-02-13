package tm.tests;

import tm.utils.HexStringConverter;

final class HexStringConverterTests {

    private HexStringConverterTests() {}

    static void runAll() {
        testBytesToHexString();
        testHexStringToBytes();
        testRoundTrip();
    }

    private static void testBytesToHexString() {
        byte[] input = new byte[] {0x00, 0x0F, 0x10, (byte)0xFF};
        String hex = HexStringConverter.bytesToHexString(input);
        TestAssertions.assertEquals("000F10FF", hex, "bytesToHexString should produce uppercase hex");
    }

    private static void testHexStringToBytes() {
        byte[] expected = new byte[] {0x01, 0x23, 0x45, 0x67, (byte)0x89, (byte)0xAB, (byte)0xCD, (byte)0xEF};
        byte[] actual = HexStringConverter.hexStringToBytes("0123456789ABCDEF");
        TestAssertions.assertArrayEquals(expected, actual, "hexStringToBytes should parse hex pairs");
    }

    private static void testRoundTrip() {
        byte[] input = new byte[] {5, 8, 13, 21, 34, 55, 89, (byte)144};
        String hex = HexStringConverter.bytesToHexString(input);
        byte[] output = HexStringConverter.hexStringToBytes(hex);
        TestAssertions.assertArrayEquals(input, output, "hex conversion should round trip");
    }
}
