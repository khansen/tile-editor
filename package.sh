#!/bin/bash

set -euo pipefail

# Ensure jpackage exists
if ! command -v jpackage >/dev/null 2>&1; then
  echo "Error: jpackage not found. Install JDK 14+ with jpackage and ensure it's on PATH."
  exit 1
fi

# Ensure JAR exists (build first)
JAR="dist/tm.jar"
if [ ! -f "$JAR" ]; then
  echo "Error: $JAR not found. Run 'ant clean build' first."
  exit 1
fi

# Package
jpackage \
  --input dist \
  --dest out \
  --name "TileManipulator" \
  --main-jar tm.jar \
  --main-class TileManipulator \
  --type dmg \
  --app-version "1.0" \
  --java-options "-Xms64m -Xmx512m"
