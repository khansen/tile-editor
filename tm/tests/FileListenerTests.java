package tm.tests;

import tm.filelistener.GameBoyAdvanceFileListener;
import tm.filelistener.GameBoyFileListener;
import tm.filelistener.INESFileListener;
import tm.filelistener.SegaMasterSystemFileListener;

final class FileListenerTests {

    private static final int[] GBA_NINTENDO_LOGO = {
        0x24,0xFF,0xAE,0x51,0x69,0x9A,0xA2,0x21,0x3D,0x84,0x82,0x0A,0x84,0xE4,0x09,0xAD,
        0x11,0x24,0x8B,0x98,0xC0,0x81,0x7F,0x21,0xA3,0x52,0xBE,0x19,0x93,0x09,0xCE,0x20,
        0x10,0x46,0x4A,0x4A,0xF8,0x27,0x31,0xEC,0x58,0xC7,0xE8,0x33,0x82,0xE3,0xCE,0xBF,
        0x85,0xF4,0xDF,0x94,0xCE,0x4B,0x09,0xC1,0x94,0x56,0x8A,0xC0,0x13,0x72,0xA7,0xFC,
        0x9F,0x84,0x4D,0x73,0xA3,0xCA,0x9A,0x61,0x58,0x97,0xA3,0x27,0xFC,0x03,0x98,0x76,
        0x23,0x1D,0xC7,0x61,0x03,0x04,0xAE,0x56,0xBF,0x38,0x84,0x00,0x40,0xA7,0x0E,0xFD,
        0xFF,0x52,0xFE,0x03,0x6F,0x95,0x30,0xF1,0x97,0xFB,0xC0,0x85,0x60,0xD6,0x80,0x25,
        0xA9,0x63,0xBE,0x03,0x01,0x4E,0x38,0xE2,0xF9,0xA2,0x34,0xFF,0xBB,0x3E,0x03,0x44,
        0x78,0x00,0x90,0xCB,0x88,0x11,0x3A,0x94,0x65,0xC0,0x7C,0x63,0x87,0xF0,0x3C,0xAF,
        0xD6,0x25,0xE4,0x8B,0x38,0x0A,0xAC,0x72,0x21,0xD4,0xF8,0x07
    };

    private static final int[] GB_SCROLLING_NINTENDO_GRAPHIC = {
        0xCE,0xED,0x66,0x66,0xCC,0x0D,0x00,0x0B,0x03,0x73,0x00,0x83,0x00,0x0C,0x00,0x0D,
        0x00,0x08,0x11,0x1F,0x88,0x89,0x00,0x0E,0xDC,0xCC,0x6E,0xE6,0xDD,0xDD,0xD9,0x99,
        0xBB,0xBB,0x67,0x63,0x6E,0x0E,0xEC,0xCC,0xDD,0xDC,0x99,0x9F,0xBB,0xB9,0x33,0x3E
    };

    private FileListenerTests() {}

    static void runAll() {
        testINESDetection();
        testSegaMasterSystemDetectionAndChecksum();
        testGameBoyDetectionAndChecksums();
        testGameBoyAdvanceDetectionAndChecksums();
    }

    private static void testINESDetection() {
        INESFileListener listener = new INESFileListener();
        byte[] data = new byte[32];
        data[0] = 0x4E;
        data[1] = 0x45;
        data[2] = 0x53;
        data[3] = 0x1A;
        TestAssertions.assertTrue(listener.doFormatDetect(data, "nes"), "iNES listener should detect valid header");
        TestAssertions.assertFalse(listener.doFormatDetect(data, "bin"), "iNES listener should reject wrong extension");
    }

    private static void testSegaMasterSystemDetectionAndChecksum() {
        SegaMasterSystemFileListener listener = new SegaMasterSystemFileListener();
        byte[] data = new byte[0x9000];
        byte[] id = "TMR SEGA".getBytes();
        for (int i = 0; i < id.length; i++) {
            data[0x7FF0 + i] = id[i];
        }

        TestAssertions.assertTrue(listener.doFormatDetect(data, "sms"), "SMS listener should detect valid header");
        TestAssertions.assertFalse(listener.doFormatDetect(data, "txt"), "SMS listener should reject wrong extension");

        listener.fileSaving(data, "sms");
        int expected = 0;
        for (int i = 0; i < 0x7FF0; i++) expected += data[i] & 0xFF;
        for (int i = 0x8000; i < data.length; i++) expected += data[i] & 0xFF;

        TestAssertions.assertEquals((byte)(expected & 0xFF), data[0x7FFA], "SMS checksum low byte should match");
        TestAssertions.assertEquals((byte)((expected >> 8) & 0xFF), data[0x7FFB], "SMS checksum high byte should match");
    }

    private static void testGameBoyDetectionAndChecksums() {
        GameBoyFileListener listener = new GameBoyFileListener();
        byte[] data = new byte[0x8000];
        for (int i = 0; i < GB_SCROLLING_NINTENDO_GRAPHIC.length; i++) {
            data[0x104 + i] = (byte)GB_SCROLLING_NINTENDO_GRAPHIC[i];
        }
        data[0x148] = 0x00; // valid ROM size

        TestAssertions.assertTrue(listener.doFormatDetect(data, "gb"), "Game Boy listener should detect valid header");
        TestAssertions.assertFalse(listener.doFormatDetect(data, "bin"), "Game Boy listener should reject wrong extension");

        listener.fileSaving(data, "gb");

        int complement = 25;
        for (int i = 0x134; i < 0x14D; i++) {
            complement += data[i] & 0xFF;
        }
        TestAssertions.assertEquals((byte)(0x100 - complement), data[0x14D], "GB complement check should match");

        int len = data.length & 0x0FFF8000;
        int checksum = 0;
        for (int i = 0; i < len; i++) {
            if (i == 0x14E || i == 0x14F) continue;
            checksum += data[i] & 0xFF;
        }
        TestAssertions.assertEquals((byte)((checksum >> 8) & 0xFF), data[0x14E], "GB checksum high byte should match");
        TestAssertions.assertEquals((byte)(checksum & 0xFF), data[0x14F], "GB checksum low byte should match");
    }

    private static void testGameBoyAdvanceDetectionAndChecksums() {
        GameBoyAdvanceFileListener listener = new GameBoyAdvanceFileListener();
        byte[] data = new byte[0x400];
        for (int i = 0; i < GBA_NINTENDO_LOGO.length; i++) {
            data[0x004 + i] = (byte)GBA_NINTENDO_LOGO[i];
        }
        data[0xB2] = (byte)0x96;

        TestAssertions.assertTrue(listener.doFormatDetect(data, "gba"), "GBA listener should detect valid header");
        TestAssertions.assertFalse(listener.doFormatDetect(data, "bin"), "GBA listener should reject wrong extension");

        listener.fileSaving(data, "gba");

        int complement = 0xE7;
        for (int i = 0xA0; i <= 0xBC; i++) {
            complement -= data[i] & 0xFF;
        }
        TestAssertions.assertEquals((byte)(complement & 0xFF), data[0xBD], "GBA complement check should match");

        int checksum = 0;
        for (int i = 0; i < data.length; i++) {
            if (i == 0xBE || i == 0xBF) continue;
            checksum += data[i] & 0xFF;
        }
        TestAssertions.assertEquals((byte)((checksum >> 8) & 0xFF), data[0xBE], "GBA checksum high byte should match");
        TestAssertions.assertEquals((byte)(checksum & 0xFF), data[0xBF], "GBA checksum low byte should match");
    }
}
