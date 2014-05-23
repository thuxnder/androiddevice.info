#~/bin/ash
adb install submit_auto.apk &&
adb shell am start -n info.androiddevice.deviceinventory.submission/info.androiddevice.deviceinventory.submission.MainActivity
