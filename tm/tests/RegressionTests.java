package tm.tests;

public class RegressionTests {

    public static void main(String[] args) throws Exception {
        AppPathsTests.runAll();
        HexStringConverterTests.runAll();
        XMLParserTests.runAll();
        ColorCodecTests.runAll();
        TileCodecTests.runAll();
        TMPaletteTests.runAll();
        FileFilterTests.runAll();
        FileListenerTests.runAll();
        BitmapUtilityTests.runAll();
        TreeNodeXmlTests.runAll();
        FileLoaderThreadTests.runAll();
        FileSaverThreadTests.runAll();
        System.out.println("All regression tests passed.");
    }
}
