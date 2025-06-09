#!/bin/bash
echo "Compiling..."
javac -cp .:mysql-connector-j.jar -d . $(find . -name "*.java")
if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi
echo "Running..."
java -cp .:mysql-connector-j.jar proiect_252.Main 