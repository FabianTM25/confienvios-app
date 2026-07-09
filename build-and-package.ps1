<#
.SYNOPSIS
    Construye la imagen de Confienvios, la empaqueta junto con postgres:16-alpine
    y copia todo lo necesario (tar + docker-compose.yml + .env) a una carpeta destino
    para entregarla al cliente (entorno sin acceso a registry).

.PARAMETER Destino
    Carpeta donde se copiaran los archivos a transferir. Se crea si no existe.

.PARAMETER Tag
    Tag de la imagen de la app. Por defecto "confienvios-app:latest".

.EXAMPLE
    .\build-and-package.ps1 -Destino "D:\Entregas\Cliente1"
#>

param(
    [Parameter(Mandatory = $true)]
    [string]$Destino,

    [string]$Tag = "confienvios-app:latest",

    [string]$PostgresImage = "postgres:16-alpine"
)

$ErrorActionPreference = "Stop"

$RepoRoot = $PSScriptRoot
Set-Location $RepoRoot

Write-Host "==> Construyendo imagen $Tag ..." -ForegroundColor Cyan
docker build -t $Tag .
if ($LASTEXITCODE -ne 0) { throw "Fallo el build de la imagen." }

Write-Host "==> Descargando/asegurando imagen $PostgresImage ..." -ForegroundColor Cyan
docker pull $PostgresImage
if ($LASTEXITCODE -ne 0) { throw "Fallo el pull de postgres." }

if (-not (Test-Path $Destino)) {
    New-Item -ItemType Directory -Path $Destino -Force | Out-Null
}

$TarPath = Join-Path $Destino "confienvios-images.tar"
Write-Host "==> Exportando imagenes a $TarPath ..." -ForegroundColor Cyan
docker save -o $TarPath $Tag $PostgresImage
if ($LASTEXITCODE -ne 0) { throw "Fallo el docker save." }

Write-Host "==> Copiando docker-compose.yml y .env ..." -ForegroundColor Cyan
Copy-Item (Join-Path $RepoRoot "docker-compose.yml") $Destino -Force

$EnvSource = Join-Path $RepoRoot ".env"
if (Test-Path $EnvSource) {
    Copy-Item $EnvSource $Destino -Force
} else {
    Write-Host "Aviso: no se encontro .env en el repo, recuerda crear uno en el destino." -ForegroundColor Yellow
}

Write-Host ""
Write-Host "==> Listo. Contenido preparado en: $Destino" -ForegroundColor Green
Get-ChildItem $Destino | Format-Table Name, Length, LastWriteTime

Write-Host ""
Write-Host "Recuerda en la maquina del cliente:" -ForegroundColor Yellow
Write-Host "  1. docker load -i confienvios-images.tar"
Write-Host "  2. En docker-compose.yml, cambiar 'build: .' por 'image: $Tag' en el servicio app"
Write-Host "  3. docker compose up -d"
