#!/bin/bash

# Path to your downloaded TexturePacker jar
TEXTURE_PACKER_JAR="./runnable-texturepacker.jar"

# Ensure packed directory exists
mkdir -p packed

for lod_dir in raw/lod_*/; do
    echo "Found: $lod_dir"
done

# Loop through each LOD folder in raw
for lod_dir in raw/lod_*; do
    lod_name=$(basename "$lod_dir")  # e.g., lod_1024
    output_dir="packed/$lod_name"

    echo "Packing $lod_dir -> $output_dir"

    # Create output directory if it doesn't exist
    mkdir -p "$output_dir"

    # Run GDX TexturePacker via Java
    java -cp "$TEXTURE_PACKER_JAR" com.badlogic.gdx.tools.texturepacker.TexturePacker \
        "$lod_dir" "$output_dir" "atlas" "packer_settings.json"
done
