#! /bin/bash

#if [ $# -ne 1 ] || [ $1 -lt 1 ] || [ $1 -gt 3 ]
#then
#  echo "Usage: $0 level[1-3]"
#  exit
#fi


APP=`head -1 app.info`
UID_FN=/data/local/tmp/app.uid

adb push app.info /data/local/tmp/

# 0. Start app from fresh
adb shell "am force-stop $APP"

# 1. Find and save UID, if needed
curr_uid=`adb shell dumpsys package $APP | grep userId | awk -F"=| " '{print $6}'`
old_uid=`adb shell "cat ${UID_FN}" | tr -d '\r\n'`

if [ "${curr_uid}" != "${old_uid}" ]
then
  echo "Updating UID"
  adb shell "echo ${curr_uid} > ${UID_FN}"
  adb shell "cat ${UID_FN}"
else
  echo "UID OK"
fi

# 2. Start command
ant build
adb push bin/TestApp.jar /data/local/tmp/
adb shell /data/local/tmp/haos runtest TestApp.jar -c nsl.stg.tests.LaunchApp

