#!/bin/bash

# Build docker image
docker build --tag "musekit_build" .

# Launch docker image and build apk
docker run --rm -it \
  -v "$(pwd):/musekit" \
  -v "android_sdk:/home/ubuntu/Android" \
  -v "gradle_cache:/home/ubuntu/.gradle" \
  -w /musekit \
  -u "$(id -u):$(id -g)" \
  "musekit_build" \
  bash -c "./gradlew assembleRelease"

# Setup signing
BUILD_TOOLS_LATEST=$(ls "$ANDROID_HOME/build-tools" | grep -v -- '-rc' | sort -V | tail -n 1)
IN="app/build/outputs/apk/release/app-release-unsigned.apk"
OUT="app/release/app-release.apk"

# Sign apk
$ANDROID_HOME/build-tools/$BUILD_TOOLS_LATEST/apksigner sign \
  --ks "$MUSEKIT_KEYSTORE_FILE" \
  --in "$IN" --out "$OUT"

