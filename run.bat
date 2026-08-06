@echo off
echo Iniciando TaskFlow...

if not exist "out" (
    echo El directorio 'out' no existe. Ejecutando compile.bat primero...
    call compile.bat
)

java -cp "out;lib/*" com.taskflow.Main
