# Facebook Marketplace Viewer 🛍️

A dedicated Android application for browsing Facebook Marketplace with enhanced UI, draggable controls, and advanced image viewing.

[![Android CI/CD](https://github.com/CALEBPOTZ/FBM-app/actions/workflows/android-build.yml/badge.svg)](https://github.com/CALEBPOTZ/FBM-app/actions/workflows/android-build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Platform](https://img.shields.io/badge/Platform-Android%208.0%2B-green.svg)](https://developer.android.com)

## 📱 Features

### Core Functionality
- **Clean Interface** - Browse Marketplace without Facebook's distractions
- **WebView-Based** - Leverages Facebook's existing marketplace interface
- **Enhanced UI** - JavaScript injection to hide unnecessary navigation
- **Messenger Integration** - Deep links to Facebook Messenger for conversations
- **Persistent Login** - Cookie-based authentication with saved session state

### Recent Enhancements ✨

#### 1. Draggable Saved Listings Button ⭐
- **Long-press** (500ms) to enable drag mode
- Position **persists** across app restarts
- Tap to navigate, drag to reposition
- Visual feedback during dragging

#### 2. Advanced Image Viewer 🖼️
- **Tap any image** to open fullscreen viewer
- **Pinch-to-zoom** (1x to 5x scale)
- **Pan/drag** when zoomed in
- **Swipe** left/right to navigate between images
- Close button and image counter

#### 3. Search Enter Key Support 🔍
- Press **Enter/Return** to execute search
- No need to wait for autocomplete
- Instant results

#### 4. Form Layout Fix 📝
- Auto-resizing textareas
- No more layout breaking
- Proper overflow handling

## 📥 Download

### Latest Release
Download the latest APK from the [Releases](https://github.com/CALEBPOTZ/FBM-app/releases) page.

### Automatic Builds
Every push to `main` triggers an automatic build. The APK is available as an artifact in [Actions](https://github.com/CALEBPOTZ/FBM-app/actions).

## 🚀 Installation

1. Download `app-debug.apk` from [Releases](https://github.com/CALEBPOTZ/FBM-app/releases)
2. Enable "Install from Unknown Sources" on your Android device:
   - Settings → Security → Unknown Sources (Android < 8)
   - Settings → Apps & notifications → Special app access → Install unknown apps (Android 8+)
3. Install the APK
4. Open and enjoy!

## 🛠️ Development

### Requirements
- Android Studio Arctic Fox (2020.3.1) or newer
- JDK 8 or higher
- Android SDK API 34 (Android 14)
- Minimum Android Version: API 26 (Android 8.0)

### Setup
```bash
# Clone the repository
git clone https://github.com/CALEBPOTZ/FBM-app.git
cd FBM-app

# Build the project
./gradlew build

# Install on device/emulator
./gradlew installDebug
```

### Project Structure
```
FBM/
├── app/
│   ├── src/main/
│   │   ├── java/com/marketplace/viewer/
│   │   │   ├── MainActivity.java              # Main coordinator
│   │   │   ├── auth/                          # Authentication
│   │   │   ├── config/                        # URLs & User Agents
│   │   │   ├── injection/                     # JavaScript injection
│   │   │   │   └── JsInjectorFixed.java      # Enhanced features
│   │   │   ├── messenger/                     # Deep linking
│   │   │   └── webview/                       # Custom WebView
│   │   ├── res/                               # Android resources
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── .github/workflows/
│   └── android-build.yml                      # CI/CD pipeline
├── README.md                                   # This file
├── BUG_FIXES.md                               # Detailed fix documentation
└── QUICKSTART.md                              # Quick start guide
```

## 🔧 Building Variants

### Debug Build
```bash
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk
```

### Release Build
```bash
./gradlew assembleRelease
# Output: app/build/outputs/apk/release/app-release-unsigned.apk
```

## 🧪 Testing

### Unit Tests
```bash
./gradlew test
```

### Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

## 📋 Permissions

- `INTERNET` - Required for loading Facebook
- `ACCESS_FINE_LOCATION` - Optional, for nearby listings
- `ACCESS_COARSE_LOCATION` - Optional, for nearby listings
- `READ_MEDIA_IMAGES` - Optional, for uploading photos

## 🏗️ Architecture

The app uses a simplified MVC/MVP pattern:
- **Model**: Configuration objects and managers
- **View**: Custom WebView and Activity layouts  
- **Controller**: MainActivity coordinates all components

### Key Components

- **MainActivity** - Main coordinator for app functionality
- **JsInjectorFixed** - Enhanced JavaScript injection with all fixes
- **AuthManager** - Manages Facebook login state via cookies
- **MessengerDeepLinker** - Handles deep links to Messenger app
- **MarketplaceWebView** - Custom WebView with marketplace-specific behavior

## 🔄 CI/CD Pipeline

GitHub Actions automatically:
1. Builds debug APK on every push
2. Runs tests
3. Uploads artifacts
4. Creates releases with changelog

View workflow: [android-build.yml](.github/workflows/android-build.yml)

## 📝 Changelog

See [BUG_FIXES.md](BUG_FIXES.md) for detailed information about recent fixes and improvements.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📜 License

This project is licensed under the MIT License - see below for details.

```
MIT License

Copyright (c) 2026 Caleb Potter

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## ⚠️ Disclaimer

This application is not affiliated with, endorsed by, or sponsored by Facebook/Meta. Use at your own risk and ensure compliance with Facebook's Terms of Service.

## 💬 Support

For issues, questions, or suggestions, please [open an issue](https://github.com/CALEBPOTZ/FBM-app/issues).

## 🌟 Star History

[![Star History Chart](https://api.star-history.com/svg?repos=CALEBPOTZ/FBM-app&type=Date)](https://star-history.com/#CALEBPOTZ/FBM-app&Date)

---

Made with ❤️ by Caleb Potter
