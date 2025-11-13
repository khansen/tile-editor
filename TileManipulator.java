/*
*
*    Copyright (C) 2003 Kent Hansen.
*
*    This file is part of Tile Manipulator.
*
*    Tile Manipulator is free software; you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation; either version 2 of the License, or
*    (at your option) any later version.
*
*    Tile Manipulator is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*/

import tm.ui.TMUI;
import java.awt.Desktop;
import java.awt.Taskbar;
import java.awt.desktop.AboutHandler;
import java.awt.desktop.AboutEvent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.io.File;
import java.net.URL;

/**
*
* Tile Manipulator main class.
* A quite pointless class really. The application is very UI-centric,
* so the TMUI class evolved into the real application backbone.
* This class just gets the show started.
*
**/

public class TileManipulator {

    /**
    *
    * Constructor.
    *
    **/
    public TileManipulator() {
        new TMUI();
    }

    /**
    *
    * Starts up the program.
    *
    **/
    public static void main(String[] args) {
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("apple.awt.application.name", "Tile Manipulator");
            System.setProperty("dock.name", "Tile Manipulator");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Tile Manipulator");
            System.setProperty("-Xdock:name", "Tile Manipulator");

            // locate icon resource (classpath first, then project resources)
            URL url = TileManipulator.class.getResource("/resources/tile-icon-512.png");
            if (url == null) url = TileManipulator.class.getResource("/tile-icon-512.png");
            if (url == null) {
                File f = new File("resources/tile-icon-512.png");
                if (f.exists()) {
                    try { url = f.toURI().toURL(); } catch (Exception ignored) {}
                }
            }

            final Image iconImage = (url != null) ? new ImageIcon(url).getImage() : null;

            if (iconImage != null) {
                try {
                    // Java 9+ Taskbar API
                    Taskbar.getTaskbar().setIconImage(iconImage);
                } catch (Throwable t) {
                    // fallback to Apple-specific API if available
                    try {
                        Class<?> appClass = Class.forName("com.apple.eawt.Application");
                        Object app = appClass.getMethod("getApplication").invoke(null);
                        appClass.getMethod("setDockIconImage", java.awt.Image.class).invoke(app, iconImage);
                    } catch (Throwable ignore) {
                        // ignore if no fallback available
                    }
                }
            } else {
                System.err.println("Dock icon not found: looked for /resources/tile-icon-512.png or /tile-icon-512.png or resources/tile-icon-512.png");
            }

            // register About handler (Java 9+)
            try {
                Desktop.getDesktop().setAboutHandler(new AboutHandler() {
                    @Override
                    public void handleAbout(AboutEvent e) {
                        SwingUtilities.invokeLater(() -> {
                            ImageIcon icon = (iconImage != null) ? new ImageIcon(iconImage) : null;
                            JOptionPane.showMessageDialog(null,
                                "Tile Manipulator\nVersion 1.0.1\n(c) 2025 Kent Hansen",
                                "About Tile Manipulator",
                                JOptionPane.INFORMATION_MESSAGE,
                                icon);
                        });
                    }
                });
            } catch (Throwable ignore) {
                // older JDKs or unsupported platform: ignore
            }
        }

        new TileManipulator();
    }
}
