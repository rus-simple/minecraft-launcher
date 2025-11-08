@echo off
chcp 65001 > nul
title Minecraft Launcher

echo ===============================
echo    Minecraft Custom Launcher
echo ===============================

:: Проверяем наличие Java
where java > nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java not found in PATH
    echo Install Java 21 or higher
    pause
    exit /b 1
)

:: Проверяем версию Java
for /f "tokens=3" %%i in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set JAVA_VERSION=%%i
)
echo Java version: %JAVA_VERSION%

:: Проверяем наличие config.properties
if not exist "config.properties" (
    echo ERROR: config.properties not found
    echo Create it from config.example.properties
    pause
    exit /b 1
)

:: Получаем путь к gameDir из config.properties
set GAME_DIR=
for /f "tokens=2 delims==" %%i in ('findstr "gameDir" config.properties') do (
    set GAME_DIR=%%i
)

:: Убираем кавычки если есть
set GAME_DIR=%GAME_DIR:"=%

:: Проверяем существование папки Minecraft
if not exist "%GAME_DIR%" (
    echo ERROR: Minecraft folder not found: %GAME_DIR%
    pause
    exit /b 1
)

echo Minecraft folder: %GAME_DIR%
echo.

:: Компилируем
echo Compiling...
javac -d out *.java
if %errorlevel% neq 0 (
    echo Compilation ERROR
    pause
    exit /b 1
)

echo Launching Minecraft...
echo.

:: Запускаем
java -cp "out" MinecraftLauncher

pause
