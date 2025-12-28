#!/bin/bash
echo "Launching OpenTrader via Maven (Development Mode)..."
# Using javafx:run handles classpath and native libraries much better on macOS
./maven/bin/mvn clean javafx:run
