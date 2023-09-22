@echo off
setlocal enabledelayedexpansion

cd C:\elasticsearch-6.4.3\bin

start cmd /k elasticsearch.bat

cd C:\kafka_2.13-3.5.1

set "folder1=kafka_2.13-3.5.1datalog"
set "folder2=kafka_2.13-3.5.1datazookeeper"

if exist "%folder1%" (
    rmdir /s /q "%folder1%"
)

if exist "%folder2%" (
    rmdir /s /q "%folder2%"
)

echo Starting ZooKeeper...
start cmd /k bin\windows\zookeeper-server-start.bat config\zookeeper.properties

:: Wait for ZooKeeper to start (adjust sleep duration if needed)
timeout /t 3

echo Starting Kafka Broker...
start cmd /k bin\windows\kafka-server-start.bat config\server.properties
