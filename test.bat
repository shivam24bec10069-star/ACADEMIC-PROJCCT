@echo off
if not exist bin\com\logiflow\test\LogiFlowTestSuite.class (
    echo Compiling test suite...
    call build.bat
)

java -cp bin com.logiflow.test.LogiFlowTestSuite
