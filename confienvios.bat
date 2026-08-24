@echo off
title Sistema Confienvios

echo ===============================
echo Iniciando sistema Confienvios...
echo ===============================

:: Ir a la carpeta del proyecto
cd /d "C:\Users\YuderTM\Documents\Cursos\Cursos\Confienvios_java_local\Confienvios_java"
if errorlevel 1 (
    echo.
    echo ERROR: no se encontro la carpeta del proyecto.
    echo Revisa la ruta configurada dentro de confienvios.bat
    pause
    exit /b 1
)

echo.
echo Verificando si Docker Desktop esta activo...
docker info >nul 2>&1
if errorlevel 1 (
    echo Docker no esta corriendo. Iniciando Docker Desktop...
    start "" "C:\Program Files\Docker\Docker\Docker Desktop.exe"
    echo Esperando a que Docker termine de iniciar...
    :esperar_docker
    timeout /t 3 >nul
    docker info >nul 2>&1
    if errorlevel 1 goto esperar_docker
)

echo.
echo Verificando si el sistema ya esta corriendo...
curl -s -o nul http://localhost:8085
if errorlevel 1 (
    echo Iniciando contenedores de Confienvios ^(docker compose^)...
    docker compose up -d
    if errorlevel 1 (
        echo.
        echo ERROR: fallo "docker compose up". Revisa el log de Docker.
        pause
        exit /b 1
    )
) else (
    echo El sistema ya esta corriendo.
)

echo.
echo Esperando a que el sistema responda en el puerto 8085...
:esperar_app
timeout /t 3 >nul
curl -s -o nul http://localhost:8085
if errorlevel 1 goto esperar_app

echo.
echo Abriendo sistema en el navegador...
start http://localhost:8085

echo.
echo ===============================
echo Sistema iniciado correctamente 🚀
echo ===============================
pause
