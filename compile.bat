@echo off
echo Compilando TaskFlow...

if not exist "out" mkdir out

javac -cp "lib/*" -d out src/main/java/com/taskflow/model/*.java src/main/java/com/taskflow/repository/*.java src/main/java/com/taskflow/service/*.java src/main/java/com/taskflow/ui/*.java src/main/java/com/taskflow/*.java

if %ERRORLEVEL% equ 0 (
    echo Compilacion exitosa.
) else (
    echo Error en la compilacion.
)
pause
