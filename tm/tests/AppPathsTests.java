package tm.tests;

import tm.AppPaths;

import java.nio.file.Path;
import java.nio.file.Paths;

final class AppPathsTests {

    private AppPathsTests() {}

    static void runAll() {
        testMacPathResolution();
        testLinuxFallbackPathResolution();
    }

    private static void testMacPathResolution() {
        String oldOs = System.getProperty("os.name");
        String oldHome = System.getProperty("user.home");
        try {
            System.setProperty("os.name", "Mac OS X");
            System.setProperty("user.home", "/Users/tester");
            Path path = AppPaths.appDataDir("Tile Manipulator");
            TestAssertions.assertEquals(
                Paths.get("/Users/tester", "Library", "Application Support", "Tile Manipulator"),
                path,
                "AppPaths should resolve macOS app support directory");
        } finally {
            System.setProperty("os.name", oldOs);
            System.setProperty("user.home", oldHome);
        }
    }

    private static void testLinuxFallbackPathResolution() {
        String oldOs = System.getProperty("os.name");
        String oldHome = System.getProperty("user.home");
        try {
            System.setProperty("os.name", "Linux");
            System.setProperty("user.home", "/home/tester");
            Path path = AppPaths.appDataDir("Tile Manipulator");
            TestAssertions.assertEquals(
                Paths.get("/home/tester", ".local", "share", "Tile Manipulator"),
                path,
                "AppPaths should resolve Linux fallback data directory");
        } finally {
            System.setProperty("os.name", oldOs);
            System.setProperty("user.home", oldHome);
        }
    }
}
