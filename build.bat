@echo off
setlocal
echo ================================================================
echo Building LogiFlow Pro (Java SE)...
echo ================================================================

if not exist bin mkdir bin

powershell -NoProfile -Command "Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { '\"' + ($_.FullName -replace '\\', '/') + '\"' } | Set-Content sources.txt"
javac -encoding UTF-8 -d bin "@sources.txt"
set BUILD_STATUS=%ERRORLEVEL%
if exist sources.txt del sources.txt

if %BUILD_STATUS% equ 0 (
    echo [BUILD SUCCESS] Project compiled into bin\
) else (
    echo [BUILD FAILURE] Compilation errors encountered.
    exit /b %BUILD_STATUS%
)
