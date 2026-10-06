#!/bin/sh
set -e
chmod +x gradlew
./gradlew build
mkdir -p OUTPUT && cp build/libs/palmganger-*.jar OUTPUT/
echo "ГОТОВО: файл мода в папке OUTPUT -> положи в .minecraft/mods"
