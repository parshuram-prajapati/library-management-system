# Firebase Credentials Setup Script
# Place your firebase-service-account.json in the project root, then run this script

# Check if firebase-service-account.json exists
if (-Not (Test-Path "firebase-service-account.json")) {
    Write-Host "❌ firebase-service-account.json not found in the project root"
    Write-Host "Please place your Firebase service account key file there first"
    exit 1
}

# Read the JSON file and convert to Base64
$jsonContent = Get-Content "firebase-service-account.json" -Raw
$base64String = [Convert]::ToBase64String([System.Text.Encoding]::UTF8.GetBytes($jsonContent))

# Set environment variable
[Environment]::SetEnvironmentVariable("FIREBASE_CREDENTIALS_BASE64", $base64String, [EnvironmentVariableTarget]::User)

Write-Host "✅ Firebase credentials set successfully!"
Write-Host "Note: Please restart your terminal or application for the changes to take effect"
