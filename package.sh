#!/bin/bash

set -euo pipefail

# Ensure jpackage exists
if ! command -v jpackage >/dev/null 2>&1; then
  echo "Error: jpackage not found. Install JDK 14+ with jpackage and ensure it's on PATH"
  exit 1
fi

# Check for icon file
if [ ! -f "TileManipulator.icns" ]; then
  echo "Error: TileManipulator.icns not found. Generate it first using iconutil"
  exit 1
fi

# Check for JAR file
if [ ! -f "dist/tm.jar" ]; then
  echo "Error: dist/tm.jar not found. Run 'ant clean build' first"
  exit 1
fi

# Package the application
jpackage \
  --input dist \
  --dest out \
  --name "Tile Manipulator" \
  --app-version "1.0" \
  --main-jar tm.jar \
  --main-class TileManipulator \
  --icon TileManipulator.icns \
  --type dmg \
  --mac-package-identifier "com.kenthansen.tilemanipulator" \
  --mac-package-name "Tile Manipulator" \
  --mac-package-signing-prefix "com.kenthansen" \
  --java-options "-Xms64m -Xmx512m" \
  --java-options "-Dapple.laf.useScreenMenuBar=true" \
  --verbose

echo "Created out/Tile Manipulator.dmg"
