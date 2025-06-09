@echo off
echo Compiling...
javac -cp ".;lib/*" -d . Main.java
if errorlevel 1 (
    echo Compilation failed!
    pause
    exit /b 1
)
echo Running...
java -cp ".;lib/*" proiect_252.Main
pause 