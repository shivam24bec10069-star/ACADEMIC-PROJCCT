#!/usr/bin/env bash
set -e

if [ ! -f "bin/com/logiflow/test/LogiFlowTestSuite.class" ]; then
    echo "Compiling test suite..."
    bash build.sh
fi

java -cp bin com.logiflow.test.LogiFlowTestSuite
