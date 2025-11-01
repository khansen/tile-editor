#!/bin/bash
# generate rounded-corner icons for .iconset and .icns
SRC="resources/tile-icon-512.png"
OUTDIR="TileManipulator.iconset"

if ! command -v convert >/dev/null 2>&1; then
  echo "Install ImageMagick: brew install imagemagick"
  exit 1
fi

mkdir -p "$OUTDIR"

magick -background none ./tile-icon.svg -resize 512x512 $SRC

# create rounded PNGs at common sizes; radius = size/8
for SIZE in 512 256 128 64 32 16; do
  RADIUS=$((SIZE/8))
  OUT="$OUTDIR/icon_${SIZE}x${SIZE}.png"
  magick "$SRC" -resize ${SIZE}x${SIZE} -alpha set \
    \( -size ${SIZE}x${SIZE} xc:none -fill white -draw "roundrectangle 0,0,$((SIZE-1)),$((SIZE-1)),$RADIUS,$RADIUS" \) \
    -compose DstIn -composite "$OUT"
done

# create @2x variants expected by iconutil
cp "$OUTDIR/icon_32x32.png"  "$OUTDIR/icon_16x16@2x.png"
cp "$OUTDIR/icon_64x64.png"  "$OUTDIR/icon_32x32@2x.png"
cp "$OUTDIR/icon_256x256.png" "$OUTDIR/icon_128x128@2x.png"
cp "$OUTDIR/icon_512x512.png" "$OUTDIR/icon_256x256@2x.png"

# generate .icns
iconutil -c icns "$OUTDIR" -o TileManipulator.icns

echo "Created TileManipulator.icns"
