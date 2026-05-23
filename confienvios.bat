@echo off
title Sistema Confienvios

echo ===============================
echo Iniciando sistema Confienvios...
echo ===============================

:: Ir a la carpeta del proyecto (IMPORTANTE /d para cambiar de disco)
cd /d "C:\Cursos\Confienvios_java\YuderTM\target"

echo.
echo Levantando servidor Spring Boot...
start cmd /k java -jar YuderTM-0.0.1-SNAPSHOT.jar

echo Esperando a que el servidor inicie...
timeout /t 10 >nul

echo.
echo Abriendo sistema en el navegador...
start http://localhost:8080

echo.
echo ===============================
echo Sistema iniciado correctamente 🚀
echo ===============================
pause