#!/usr/bin/env bash
set -e
echo "================================================================"
echo "Building LogiFlow Pro (Java SE)..."
echo "================================================================"

mkdir -p bin
find src -name "*.java" > sources.txt
javac -encoding UTF-8 -d bin @sources.txt
rm sources.txt

echo "[BUILD SUCCESS] Project compiled into bin/"
