package tm;

import java.nio.file.*;

public final class SettingsManager {
    private static final String FILE_NAME = "settings.xml";

    public static Path settingsFile() {
        Path dir = AppPaths.appDataDir("Tile Manipulator");
        try {
            Files.createDirectories(dir);
        } catch (Exception e) {
            System.err.println("Failed to create settings directory: " + e.getMessage());
        }
        return dir.resolve(FILE_NAME);
    }
}

