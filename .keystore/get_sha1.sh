#!/bin/bash
# Script to get SHA-1 fingerprint for Google Sign-In

echo "==================================="
echo "Rise30 Debug Keystore SHA-1"
echo "==================================="
echo ""

KEYSTORE_PATH="${PROJECT_ROOT:-$(dirname "$0")}/debug.keystore"

if [ ! -f "$KEYSTORE_PATH" ]; then
    echo "Error: Keystore not found at $KEYSTORE_PATH"
    exit 1
fi

echo "SHA-1 Fingerprint:"
keytool -list -v -keystore "$KEYSTORE_PATH" -alias androiddebugkey -storepass android -keypass android 2>/dev/null | grep "SHA1" | awk '{print $2}'

echo ""
echo "SHA-256 Fingerprint:"
keytool -list -v -keystore "$KEYSTORE_PATH" -alias androiddebugkey -storepass android -keypass android 2>/dev/null | grep "SHA256" | awk '{print $2}'

echo ""
echo "==================================="
echo "Add this SHA-1 to Google Cloud Console:"
echo "https://console.cloud.google.com/apis/credentials"
echo "==================================="
