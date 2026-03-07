RISE30 DEBUG KEYSTORE SETUP
===========================

CURRENT SHA-1 FINGERPRINT:
--------------------------
E2:2E:EF:21:B2:A9:3E:E7:4F:22:DD:86:D4:DD:30:41:1C:95:B0:61

SHA-256 FINGERPRINT:
--------------------
89:6E:27:C0:90:15:A7:F4:D6:46:D3:AC:9B:3D:19:CD:7D:D6:10:5C:2E:FB:CC:07:86:0D:29:49:9F:9F:93:05

GOOGLE CLOUD CONSOLE SETUP:
---------------------------
1. Go to: https://console.cloud.google.com/apis/credentials
2. Find your Android OAuth 2.0 Client ID
3. Add this SHA-1 fingerprint:
   E2:2E:EF:21:B2:A9:3E:E7:4F:22:DD:86:D4:DD:30:41:1C:95:B0:61
4. Save changes
5. Wait 5-10 minutes for changes to propagate

HOW THIS SOLVES THE PROBLEM:
----------------------------
- The debug.keystore file is now in the project at: .keystore/debug.keystore
- Gradle is configured to use this keystore for debug builds
- All developers/machines will use the SAME keystore
- The SHA-1 fingerprint will NEVER change

GET SHA-1 ANYTIME:
------------------
Run: ./.keystore/get_sha1.sh

OR manually:
keytool -list -v -keystore .keystore/debug.keystore -alias androiddebugkey -storepass android -keypass android

TROUBLESHOOTING:
----------------
If Google Sign-In still fails:
1. Uninstall the app: ./gradlew uninstallDebug
2. Clean build: ./gradlew clean
3. Reinstall: ./gradlew installDebug
4. Wait 10 minutes after adding SHA-1 to Google Cloud

PREVIOUS SHA-1s (add all to Google Cloud Console):
--------------------------------------------------
- 68:75:0E:7F:96:3E:0E:60:C6:69:5D:67:A7:DE:E1:CC:DF:61:72:F7 (old)
- E2:2E:EF:21:B2:A9:3E:E7:4F:22:DD:86:D4:DD:30:41:1C:95:B0:61 (current)
