@echo off
if not exist bin\com\logiflow\Main.class (
    echo Binaries not found. Invoking build script...
    call build.bat
)

java -cp bin com.logiflow.Main %*
