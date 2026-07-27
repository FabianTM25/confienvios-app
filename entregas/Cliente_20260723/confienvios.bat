@echo off
title Sistema Confienvios

echo ===============================
echo Iniciando sistema Confienvios...
echo ===============================

:: Ir a la carpeta del proyecto (IMPORTANTE /d para cambiar de disco)
cd /d "C:\Cursos\Confienvios_java_local\Confienvios_java"
if errorlevel 1 (
    echo.
    echo ERROR: no se encontro la carpeta del proyecto.
    echo Revisa la ruta configurada dentro de confienvios.bat
    pause
    exit /b 1
)

echo.
echo Verificando que Docker Desktop este activo...
docker info >nul 2>&1
if errorlevel 1 (
    echo.
    echo Docker Desktop no esta corriendo. Abriendolo...
    start "" "C:\Program Files\Docker\Docker\Docker Desktop.exe"
    echo Esperando a que Docker inicie ^(puede tardar un minuto^)...
    :esperar_docker
    timeout /t 5 >nul
    docker info >nul 2>&1
    if errorlevel 1 goto esperar_docker
)

echo.
echo Levantando backend, frontend y base de datos con Docker...
docker compose up -d

echo.
echo Esperando a que el sistema responda en el puerto 8080...
:esperar_app
timeout /t 3 >nul
curl -s -o nul http://localhost:8080
if errorlevel 1 goto esperar_app

echo.
echo Abriendo sistema en el navegador...
start http://localhost:8080

echo.
echo ===============================
echo Sistema iniciado correctamente 🚀
echo ===============================
pause