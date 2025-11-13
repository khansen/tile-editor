package tm;

import java.nio.file.*;
import java.util.Locale;

public final class AppPaths {
  public static Path appDataDir(String appName) {
    String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
    String home = System.getProperty("user.home");
    if (os.contains("mac"))  return Paths.get(home, "Library", "Application Support", appName);
    if (os.contains("win")) {
      String appData = System.getenv("APPDATA");
      if (appData != null && !appData.isEmpty()) return Paths.get(appData, appName);
    }
    String xdg = System.getenv("XDG_DATA_HOME");
    if (xdg != null && !xdg.isEmpty()) return Paths.get(xdg, appName);
    return Paths.get(home, ".local", "share", appName); // Linux fallback
  }
}

