#!/bin/bash

# Define the desired LOD sizes
sizes=(512 256 128)

# Create output folders
for size in "${sizes[@]}"; do
  mkdir -p "LOD_$size"
done

# Process each PNG file
for file in LOD_1024/*_1024.png; do
  base=$(basename "$file" .png)
  clean_name=${base%_1024}  # remove trailing _1024
  for size in "${sizes[@]}"; do
    magick "$file" -resize ${size}x${size} "LOD_$size/${clean_name}_${size}.png"
  done
done

echo "✅ LOD textures generated!"
