# VoskiAppPort - React Native Porting Package

## 🎯 Project Goal
Port the Voski Lightning wallet from Android-only (Kotlin + Jetpack Compose) to cross-platform (React Native + Expo) supporting both iOS and Android from a single codebase.

## 📦 What's Included

This repository contains everything needed to understand and port the Voski wallet:

### Documentation (`docs/`)
| File | Purpose |
|------|---------|
| **CONTRACTOR_BRIEFING.md** | ⭐ **START HERE** - Complete contractor guide |
| Purpose.md | What Voski is and why it exists |
| PLAN.md | React Native migration architecture plan |
| BackgroundInfo.txt | Technical feasibility and Lightning architecture |
| LOCAL_TESTING_GUIDE.md | How to test locally with mock backend |
| IMPLEMENTATION_PLAN.md | Development phases and timeline |
| TRANSLATION.md | Localization information |
| CHANGELOG.md | Development history |
| CLAUDE.md | Development guidelines |

### Source Code (`source/`)
| Directory | Contents |
|-----------|----------|
| **src/** | Complete Android application source (222 Kotlin files) |
| **voski-shared/** | Shared business logic module (Kotlin Multiplatform-ready) |

### Configuration (`config/`)
| File | Purpose |
|------|---------|
| build.gradle.kts | Gradle build configuration and dependencies |
| settings.gradle.kts | Gradle project settings |
| gradle.properties | Build properties and flags |
| proguard-rules.pro | Code obfuscation rules for release builds |
| .gitignore | Git ignore patterns |
| gradlew / gradlew.bat | Gradle wrapper scripts |

## 🚀 Quick Start

### For Contractors

1. **Read First**: Open `CONTRACTOR_BRIEFING.md` for complete project overview
2. **Understand Architecture**: Read `docs/Purpose.md` and `docs/PLAN.md`
3. **Review Source**: Browse `source/src/` to understand current implementation
4. **Plan Port**: Use `CONTRACTOR_BRIEFING.md` to create your approach
5. **Ask Questions**: Contact project owner with any questions

### For Reviewers

1. Review project scope in `CONTRACTOR_BRIEFING.md`
2. Check source code complexity in `source/src/`
3. Understand business logic in `source/voski-shared/`
4. Review dependencies in `config/build.gradle.kts`

## 📊 Project Stats

- **Language**: Kotlin (Android)
- **Target**: React Native + TypeScript (iOS + Android)
- **Source Files**: 222 Kotlin files
- **Resource Files**: 156 (layouts, strings, icons)
- **Lines of Code**: ~15,000-20,000 (estimated)
- **Features**: Lightning wallet, LSP integration, encrypted backups, push notifications

## 🎯 Key Features to Port

### Core Features
- ⚡ Lightning Network send/receive
- 🔐 Self-custody with seed phrase backup
- 📱 PIN + biometric authentication
- 📷 QR code scanning (invoices, vouchers)
- 🔄 Encrypted cloud backups (iCloud/Google Drive)
- 🎁 Voucher/gift card redemption (LNURL-withdraw)
- 🔔 Push notifications for offline receiving

### Technical Features
- 🌩️ Voltage LSP integration
- 🔒 Secure key storage (Keychain/Keystore)
- 📡 Push-to-wake for offline payments
- 🔐 Channel state backup & restore
- 🧩 Static Channel Backup (SCB) fallback

## 🏗️ Current Architecture

```
Voski Android App (Kotlin)
├── UI Layer (Jetpack Compose)
│   ├── Home Screen (balance, history)
│   ├── Payments (send/receive)
│   ├── Settings (backup, security)
│   └── Auth (PIN, biometric)
├── Business Logic (voski-shared)
│   ├── Wallet Management
│   ├── Lightning Client
│   ├── LSP Client (Voltage)
│   └── Backup Manager
├── Data Layer
│   ├── DataStore (preferences)
│   ├── SQLDelight (database)
│   └── Secure Storage (Keystore)
└── Platform Services
    ├── Push Notifications (FCM)
    ├── Camera (QR scanning)
    └── Background Tasks
```

## 🎯 Target Architecture

```
Voski React Native App (TypeScript)
├── UI Layer (React Native)
│   ├── Navigation (React Navigation)
│   ├── Screens (functional components)
│   └── Components (reusable UI)
├── Business Logic (TypeScript)
│   ├── Wallet Manager
│   ├── Lightning Client
│   ├── LSP Client (Voltage)
│   └── Backup Manager
├── State Management
│   ├── Redux / Zustand / Context
│   └── Async actions
├── Data Layer
│   ├── SecureStore (keys)
│   ├── AsyncStorage (preferences)
│   └── SQLite (database)
├── Native Bridges
│   ├── Crypto (secp256k1, BIP39)
│   ├── Biometrics
│   └── Secure Storage
└── Services
    ├── Push Notifications (Expo/FCM)
    ├── Camera (QR scanning)
    └── Background Fetch
```

## ⚠️ Critical Requirements

### Security (Non-Negotiable)
- ✅ Private keys NEVER leave device
- ✅ All backups encrypted with seed-derived key
- ✅ Secure storage using Keychain (iOS) / Keystore (Android)
- ✅ No secrets in code, logs, or analytics

### Performance
- ✅ Crypto operations must be native (not JavaScript)
- ✅ App launch < 2 seconds
- ✅ Payment sending < 3 seconds
- ✅ QR scanning responsive

### Functionality
- ✅ Feature parity with Android version
- ✅ Voltage LSP integration maintained
- ✅ Push-to-wake for offline receiving
- ✅ Encrypted backups to iCloud/Google Drive
- ✅ Seed-based recovery

## 📋 Deliverables

### Code
- [ ] Complete React Native project (Expo recommended)
- [ ] All Kotlin source ported to TypeScript
- [ ] Native modules for crypto operations
- [ ] Unit and integration tests
- [ ] E2E tests for critical flows

### Builds
- [ ] iOS build (TestFlight-ready)
- [ ] Android build (APK/AAB for Play Store)
- [ ] Build scripts and configuration

### Documentation
- [ ] Setup and installation guide
- [ ] Architecture documentation
- [ ] API integration guide
- [ ] Deployment guide (App Store + Play Store)
- [ ] Known issues and future work

## 📅 Timeline Estimate

| Phase | Duration | Tasks |
|-------|----------|-------|
| 1. Setup | 2-3 days | Project init, TypeScript, navigation |
| 2. Core Logic | 5-7 days | Wallet, Lightning, LSP, backup porting |
| 3. Native Bridges | 3-5 days | Crypto modules, secure storage |
| 4. UI | 5-7 days | Screens, components, flows |
| 5. Integration | 3-5 days | Testing, bug fixes, optimization |
| 6. Polish | 2-3 days | Documentation, deployment prep |
| **Total** | **3-4 weeks** | For experienced React Native + Bitcoin dev |

## 🔗 Useful Resources

### Reference Projects
- **BlueWallet**: React Native Bitcoin wallet (https://github.com/BlueWallet/BlueWallet)
- **Zeus**: React Native Lightning wallet (https://github.com/ZeusLN/zeus)

### Documentation
- React Native: https://reactnative.dev/
- Expo: https://docs.expo.dev/
- Lightning Dev Kit: https://lightningdevkit.org/
- Voltage LSP: (API docs provided separately)

### Libraries
- react-native-crypto
- react-native-bip39
- expo-secure-store
- react-native-vision-camera
- react-native-qrcode-svg

## 📞 Contact

For questions or clarifications:
1. Review documentation in `docs/` first
2. Check source code comments
3. Contact project owner

## 🛠️ Development Workflow

### Recommended Approach
1. **Week 1**: Setup + Core business logic porting
2. **Week 2**: Native bridges + Critical screens
3. **Week 3**: Remaining UI + Integration testing
4. **Week 4**: Polish + Documentation + Deployment prep

### Testing Strategy
1. **Unit Tests**: Business logic (wallet, Lightning, crypto)
2. **Integration Tests**: LSP integration, backup/restore
3. **E2E Tests**: Complete user flows (send, receive, restore)
4. **Manual Testing**: Both iOS and Android devices
5. **Performance Tests**: Crypto operation benchmarks

## ⚡ Getting Help

### Before You Start
- Have you built Bitcoin/Lightning wallets before?
- Familiar with React Native native modules?
- Have access to iOS and Android test devices?
- Understand Lightning Network basics?

### During Development
- Refer to source code in `source/src/`
- Check business logic in `source/voski-shared/`
- Use `LOCAL_TESTING_GUIDE.md` for testing
- Review dependencies in `config/build.gradle.kts`

## 📄 License

*[License information to be determined by project owner]*

## 🌟 Success Criteria

The port is successful when:
- ✅ App launches on both iOS and Android
- ✅ Can generate and restore from seed phrase
- ✅ Can send Lightning payments via QR scan
- ✅ Can receive Lightning payments (online and offline)
- ✅ Push notifications wake app for payments
- ✅ Encrypted backups work on both platforms
- ✅ Voucher redemption works
- ✅ All security requirements met
- ✅ Performance meets requirements
- ✅ No critical bugs

---

**Start with `CONTRACTOR_BRIEFING.md` for the complete guide! 🚀**
