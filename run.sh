#!/usr/bin/env bash
set -e

if [ ! -f "bin/com/logiflow/Main.class" ]; then
    echo "Binaries not found. Running build.sh first..."
    bash build.sh
fi

java -cp bin com.logiflow.Main "$@"
