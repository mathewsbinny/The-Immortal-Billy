# Compiles the game and runs the JUnit 5 test suite.
# Usage: powershell -ExecutionPolicy Bypass -File run-tests.ps1
$ErrorActionPreference = 'Stop'
Set-Location $PSScriptRoot

$junit = 'test-lib/junit-platform-console-standalone.jar'
$junitUrl = 'https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar'
$sqlite = 'resources/sqlite-jdbc-3.43.0.0 copy.jar'

if (-not (Test-Path $junit)) {
    New-Item -ItemType Directory -Force -Path 'test-lib' | Out-Null
    Write-Host 'Downloading JUnit 5 standalone runner...'
    Invoke-WebRequest -Uri $junitUrl -OutFile $junit -UseBasicParsing
}

# Compile main sources.
$src = Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -cp "$sqlite" -d out $src
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

# Compile tests.
New-Item -ItemType Directory -Force -Path 'out-test' | Out-Null
$tests = Get-ChildItem -Recurse test -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -cp "out;$sqlite;$junit" -d out-test $tests
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

# Run tests.
java -jar $junit execute --class-path "out;out-test;$sqlite" --scan-class-path
exit $LASTEXITCODE
