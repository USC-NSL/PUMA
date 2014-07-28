#! /bin/bash

adb push haos /data/local/tmp
adb shell "chmod 0755 /data/local/tmp/haos"
adb shell "ls -l /data/local/tmp/haos"

adb shell "mkdir -p /data/local/tmp/local/tmp"
