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

set "PGBIN=C:\Users\57310\AppData\Local\pgsql-portable\pgsql\bin"
set "PGDATA=C:\Users\57310\AppData\Local\pgsql-portable\data"
set "PGLOG=C:\Users\57310\AppData\Local\pgsql-portable\pg.log"
set "APPLOG=C:\Users\57310\AppData\Local\pgsql-portable\app.log"
set "JARFILE=C:\Cursos\Confienvios_java_local\Confienvios_java\backend\target\YuderTM-0.0.1-SNAPSHOT.jar"
set "JAVAW=C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot\bin\javaw.exe"

:: Reintentos: justo despues de reiniciar el PC, el disco/servicios pueden
:: tardar en estar listos y las rutas fallar de forma transitoria.
set "intentos=0"
:verificar_archivos
set /a intentos+=1
if not exist "%PGBIN%\pg_ctl.exe" goto :reintentar_archivos
if not exist "%JAVAW%" goto :reintentar_archivos
if not exist "%JARFILE%" goto :reintentar_archivos
goto :archivos_listos

:reintentar_archivos
if %intentos% GEQ 10 (
    echo.
    echo ERROR: no se encontraron los archivos necesarios ^(PostgreSQL/Java/JAR^) tras varios intentos.
    pause
    exit /b 1
)
echo Esperando a que el sistema termine de iniciar... ^(intento %intentos%^)
timeout /t 3 >nul
goto :verificar_archivos

:archivos_listos
echo.
echo Verificando base de datos (PostgreSQL local)...
"%PGBIN%\pg_ctl.exe" -D "%PGDATA%" status >nul 2>&1
if errorlevel 1 (
    echo Iniciando PostgreSQL local...
    start "Confienvios DB" /min "%PGBIN%\pg_ctl.exe" -D "%PGDATA%" -l "%PGLOG%" -o "-p 5432" -w start
) else (
    echo PostgreSQL ya esta activo.
)

echo.
echo Verificando si el sistema ya esta corriendo...
curl -s -o nul http://localhost:8085
if errorlevel 1 (
    echo Iniciando backend Confienvios...
    set "SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/confienviosdb"
    set "SPRING_DATASOURCE_USERNAME=postgres"
    set "SPRING_DATASOURCE_PASSWORD=postgres"
    set "JWT_SECRET=lWmwAhP+N6yLrxrrl5C5vF4hSPle27RCvKLPLpWt4RAyVXUyNtlJUSYgSm3QJC6/"
    set "PORT=8085"
    start "" /b "%JAVAW%" -jar "%JARFILE%" > "%APPLOG%" 2>&1
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
