# Online APK Builder - Fast Method

Since Docker environment lacks full Android SDK, use online builder:

## Method 1: APK Builder (Fastest)

1. Go to: https://appsgeyser.com/create/start/
2. Choose "Website" template
3. Upload these files:
   - MainActivity.java
   - LockService.java  
   - BootReceiver.java
   - AndroidManifest.xml
   - device_admin.xml

4. Click "Create App"
5. Download APK in 5 minutes

## Method 2: Android Studio Cloud Build

1. https://developer.android.com/studio/run/emulator-acceleration#cloud
2. Import project from GitHub
3. Build → Generate Signed APK
4. Download

## Method 3: GitHub Actions (Automated)

Push to GitHub repo with this workflow:

```yaml
name: Build APK
on: [push]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-java@v2
        with:
          distribution: 'adopt'
          java-version: '11'
      - name: Build APK
        run: ./gradlew assembleRelease
      - uses: actions/upload-artifact@v2
        with:
          name: app-release.apk
          path: app/build/outputs/apk/release/app-release.apk
```

## Method 4: Termux on Android (Build on Phone)

```bash
pkg install openjdk-17 gradle
cd rmax-rat-apk
gradle assembleRelease
```

APK will be in: app/build/outputs/apk/release/

---

**Recommended: Use Method 1 (AppGeyser) - fastest, no install needed.**
