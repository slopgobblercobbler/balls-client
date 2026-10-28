@echo off
title Balls Client - Quick Boot & Play
echo ===================================================
echo             BALLS CLIENT - QUICK LAUNCHER
echo ===================================================
echo.
echo Choose how to boot and play:
echo   [1] Boot Minecraft 1.21.1 with Balls Client (Recommended)
echo   [2] Boot Minecraft 1.20.1 with Balls Client
echo   [3] Boot Minecraft 26.3 Snapshot with Balls Client
echo   [4] Install JAR to your standard .minecraft/mods folder
echo   [5] Run test suite
echo   [6] Exit
echo.
set /p OPT="Enter option [1-6]: "

if "%OPT%"=="1" (
    echo Starting Minecraft 1.21.1 Client with Balls Client...
    call "%~dp0gradlew.bat" :fabric-1.21.1:runClient
) else if "%OPT%"=="2" (
    echo Starting Minecraft 1.20.1 Client with Balls Client...
    call "%~dp0gradlew.bat" :fabric-1.20.1:runClient
) else if "%OPT%"=="3" (
    echo Starting Minecraft 26.3 Client with Balls Client...
    call "%~dp0gradlew.bat" :fabric-26.3:runClient
) else if "%OPT%"=="4" (
    set "MODS_DIR=%APPDATA%\.minecraft\mods"
    if not exist "%MODS_DIR%" mkdir "%MODS_DIR%"
    copy /y "%~dp0fabric-1.21.1\build\libs\fabric-1.21.1-1.0.0.jar" "%MODS_DIR%\balls-client-fabric-1.21.1-v1.0.0.jar"
    echo.
    echo Installed balls-client-fabric-1.21.1-v1.0.0.jar to %MODS_DIR%!
    pause
) else if "%OPT%"=="5" (
    call "%~dp0gradlew.bat" test
    pause
) else (
    exit /b 0
)
