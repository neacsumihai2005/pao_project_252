#!/bin/bash
echo "Compiling..."
javac -d . Main.java
if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi
echo "Running..."
java proiect_252.Main 