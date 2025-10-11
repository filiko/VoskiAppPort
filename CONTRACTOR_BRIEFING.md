# Voski React Native Port - Contractor Briefing

## Project Overview
Voski is a self-custody Lightning wallet (Bitcoin) for mobile, similar to Phoenix Wallet.
- **Current State**: Android-only, built with Kotlin + Jetpack Compose
- **Goal**: Port to React Native for iOS + Android from single codebase
- **Codebase Size**: 222 Kotlin files, 156 resource files

## Key Features to Port

### 1. Lightning Wallet Core
- Bitcoin/Lightning payment send/receive
- Invoice generation and QR scanning
- Balance display and transaction history
- Seed phrase generation and recovery (12 words)

### 2. Security
- PIN authentication with custom keyboard
- Biometric authentication (fingerprint/face ID)
- Secure key storage (encrypted, never leaves device)
- Screen lock integration

### 3. LSP Integration (Voltage)
- Lightning Service Provider integration
- Channel management and liquidity provisioning
- Push-to-wake for offline receiving
- Route hints for private channels

### 4. Backup System
- Encrypted cloud backups (iCloud/Google Drive)
- Automatic backup after every channel update
- Optional backend mirroring (encrypted)
- Restore from seed phrase + cloud backup
- Static Channel Backup (SCB) fallback

### 5. Voucher System
- LNURL-withdraw QR code scanning
- Gift card redemption flow
- Voucher validation and claiming

### 6. Push Notifications
- Firebase Cloud Messaging integration
- Wake app for incoming Lightning payments
- Hold invoice flow for offline receives

## Current Architecture

### UI Layer
- **Framework**: Jetpack Compose (declarative UI)
- **Screens**: Home, Payments (send/receive), Settings, Auth (PIN/biometric)
- **Components**: Custom PIN keyboard, QR scanner, amount input, payment history

### State Management
- **Pattern**: MVVM (Model-View-ViewModel)
- **LiveData**: Reactive data streams
- **ViewModels**: Business logic and state

### Business Logic
- **Module**: voski-shared (Kotlin Multiplatform-ready)
- **Wallet Management**: Key generation, signing, backup
- **Lightning Client**: Channel management, payment processing
- **LSP Client**: Voltage API integration

### Data Storage
- **Preferences**: DataStore (key-value storage)
- **Database**: SQLDelight (structured data)
- **Secure Storage**: Android Keystore for sensitive data

### Networking
- **HTTP**: Ktor client for LSP communication
- **WebSocket**: Real-time updates (if used)

### Camera & QR
- **CameraX**: Camera device compatibility
- **ZXing**: QR code reading/writing

### Background Processing
- **WorkManager**: Periodic backup tasks
- **Services**: Foreground service for Lightning node

## React Native Target Architecture

### Recommended Stack
**Framework**: React Native + Expo with TypeScript

### UI Layer
- **Components**: React Native core components
- **Navigation**: React Navigation 6
- **Styling**: StyleSheet or styled-components
- **Camera**: expo-camera or react-native-vision-camera
- **QR Codes**: react-native-qrcode-svg

### State Management (Choose One)
- **Option A**: Redux Toolkit (mature, extensive ecosystem)
- **Option B**: Zustand (lightweight, simpler)
- **Option C**: React Context + useReducer (minimal dependencies)

### Storage
- **Secure**: Expo SecureStore (Keychain/Keystore wrapper)
- **Preferences**: AsyncStorage
- **Database**: SQLite via expo-sqlite or WatermelonDB

### Crypto Operations (CRITICAL)
- **Native Modules Required**: Bitcoin/Lightning crypto must be native
- **secp256k1**: Elliptic curve operations (native bridge)
- **BIP39**: Mnemonic seed handling (react-native-bip39)
- **Encryption**: AES for backups (native crypto)

### Networking
- **HTTP**: Fetch API or Axios
- **WebSocket**: Native WebSocket API

### Push Notifications
- **Firebase**: via Expo Push Notifications or react-native-firebase

### Background Tasks
- **Limited on iOS**: Use Expo Background Fetch or react-native-background-actions
- **Android**: Foreground service via native module

## Critical Requirements

### Non-Negotiable
- ✅ **Self-custody**: Private keys NEVER leave device, even encrypted
- ✅ **Cross-platform**: Single codebase for iOS + Android
- ✅ **Encrypted backups**: Seed-derived key for all backups
- ✅ **Offline receiving**: Push-to-wake flow must work
- ✅ **Security**: Professional-grade key management
- ✅ **Voltage LSP**: Integration must be maintained
- ✅ **Performance**: Crypto operations must be fast enough (native)

### Technical Constraints
- Minimum iOS 13, Android SDK 26
- Support biometric authentication on both platforms
- Handle app backgrounding/foregrounding gracefully
- Network resilience (retry logic, offline mode)

## Deliverables Expected

### 1. React Native Project Setup
- Expo-managed or bare workflow (recommend Expo)
- TypeScript configuration
- Project structure matching business logic
- Development environment documentation

### 2. Source Code Port
- All 222 Kotlin files ported to TypeScript/JavaScript
- UI components recreated in React Native
- Business logic adapted from voski-shared module
- Equivalent functionality for all screens

### 3. Native Bridges
- secp256k1 crypto operations (iOS + Android)
- Secure key storage wrappers
- Any other platform-specific code needed

### 4. Testing
- Unit tests for business logic
- Integration tests for critical flows
- Manual testing on iOS and Android devices
- Performance benchmarks for crypto operations

### 5. Documentation
- Setup and installation guide
- Architecture documentation
- API integration guide (Voltage LSP)
- Deployment guide (App Store + Google Play)
- Known limitations and future work

### 6. Build Outputs
- Working iOS build (TestFlight-ready)
- Working Android build (APK/AAB)
- Build scripts and CI/CD configuration

## Timeline Estimate

### Phase 1: Setup & Architecture (2-3 days)
- Create React Native project with Expo
- Set up TypeScript and project structure
- Configure development environment
- Set up basic navigation

### Phase 2: Core Business Logic (5-7 days)
- Port wallet management from Kotlin to TypeScript
- Port Lightning network operations
- Port LSP client (Voltage integration)
- Port backup/restore logic
- Port voucher redemption

### Phase 3: Native Bridges (3-5 days)
- Implement secp256k1 native module (iOS)
- Implement secp256k1 native module (Android)
- Secure storage wrappers
- Biometric authentication bridges
- Test crypto operations performance

### Phase 4: UI Implementation (5-7 days)
- Auth screens (PIN, biometric, seed entry)
- Home screen with balance and history
- Send payment screen with QR scan
- Receive payment screen with invoice generation
- Settings screen
- Backup/restore flows
- Voucher redemption UI

### Phase 5: Integration & Testing (3-5 days)
- End-to-end integration
- Platform testing (iOS simulator, Android emulator)
- Device testing (real iOS device, real Android device)
- Performance optimization
- Bug fixes

### Phase 6: Polish & Documentation (2-3 days)
- Code cleanup and optimization
- Documentation writing
- Deployment preparation
- Handoff materials

**Total Estimate**: 3-4 weeks for experienced React Native + Bitcoin developer

## Key Challenges

### 1. Crypto Operations
- **Challenge**: JavaScript is too slow for secp256k1 operations
- **Solution**: Native modules for all Bitcoin/Lightning crypto
- **Libraries**: Use proven native crypto libraries, wrap in React Native

### 2. State Management
- **Challenge**: Complex Lightning state (channels, HTLCs, pending payments)
- **Solution**: Careful state design, possibly Redux for complex flows

### 3. Background Sync
- **Challenge**: Mobile OSes limit background execution
- **Solution**:
  - iOS: Background fetch (limited)
  - Android: Foreground service
  - Push notifications to wake app

### 4. Security
- **Challenge**: Secure key storage across platforms
- **Solution**:
  - iOS: Keychain via Expo SecureStore
  - Android: Keystore via Expo SecureStore
  - Never store keys in AsyncStorage

### 5. Performance
- **Challenge**: React Native overhead for crypto operations
- **Solution**: Native modules for all crypto, minimize JS bridge calls

### 6. Platform Differences
- **Challenge**: iOS and Android have different capabilities
- **Solution**: Abstract platform-specific code, graceful degradation

## Reference Resources

### Open Source Projects
- **BlueWallet**: React Native Bitcoin wallet (excellent reference)
  - GitHub: https://github.com/BlueWallet/BlueWallet
- **Zeus**: React Native Lightning wallet
  - GitHub: https://github.com/ZeusLN/zeus

### Libraries & Tools
- **React Native Crypto**: https://github.com/tradle/react-native-crypto
- **BIP39**: https://github.com/bitcoinjs/bip39
- **Lightning Dev Kit (LDK)**: https://lightningdevkit.org/
- **Voltage API Docs**: (provided separately)

### Documentation
- React Native: https://reactnative.dev/
- Expo: https://docs.expo.dev/
- Lightning Network: https://lightning.network/
- Bitcoin Development: https://bitcoin.org/en/developer-documentation

## Questions for Contractor

Before starting, please answer:

1. **Experience**:
   - Have you built Bitcoin or Lightning wallets before?
   - Familiar with secp256k1 and Bitcoin cryptography?

2. **React Native**:
   - Expo or bare React Native preference?
   - Experience with native modules (iOS + Android)?

3. **Security**:
   - Experience with secure key storage in mobile apps?
   - Familiar with biometric authentication on both platforms?

4. **Timeline**:
   - Can you commit to 3-4 week timeline?
   - Availability (full-time vs part-time)?

5. **Testing**:
   - What's your testing strategy?
   - Have access to iOS and Android devices?

6. **Deliverables**:
   - Can you provide App Store and Google Play ready builds?
   - CI/CD setup included?

7. **Support**:
   - Post-delivery support period?
   - Hourly rate for future maintenance?

8. **Cost**:
   - Fixed price or hourly?
   - What's your estimated total cost?

## Project Success Criteria

### Must Have (MVP)
- ✅ Wallet initialization with seed phrase generation
- ✅ Send and receive Lightning payments
- ✅ QR code scanning for invoices and vouchers
- ✅ PIN and biometric authentication
- ✅ Encrypted backup to iCloud/Google Drive
- ✅ Restore from seed phrase + backup
- ✅ Voltage LSP integration functional
- ✅ Push notifications wake app for payments
- ✅ Works on both iOS and Android

### Nice to Have (Future)
- Advanced channel management UI
- Multiple LSP support
- On-chain transaction support
- Advanced privacy features
- Widget support
- Watch-only mode

## Repository Structure

This repository contains:
- **docs/**: All documentation files
  - Purpose.md: What Voski is and why it exists
  - PLAN.md: Original migration plan
  - BackgroundInfo.txt: Technical architecture details
  - LOCAL_TESTING_GUIDE.md: How to test locally
  - IMPLEMENTATION_PLAN.md: Development phases
  - TRANSLATION.md: Localization info

- **source/**: Complete Kotlin source code
  - src/: Android application code (222 .kt files)
  - voski-shared/: Shared business logic module

- **config/**: Build and configuration files
  - build.gradle.kts: Gradle build configuration
  - settings.gradle.kts: Gradle settings
  - gradle.properties: Build properties
  - proguard-rules.pro: Code obfuscation
  - gradlew/gradlew.bat: Gradle wrappers

## Getting Started

1. **Read Documentation**: Start with Purpose.md and PLAN.md
2. **Review Architecture**: Read BackgroundInfo.txt
3. **Explore Source**: Browse source/src/ to understand structure
4. **Setup Dev Environment**: Follow your React Native + Expo setup
5. **Create Project**: Initialize new React Native + Expo project
6. **Start Porting**: Begin with core business logic (voski-shared)
7. **Build UI**: Recreate screens in React Native
8. **Test**: Use LOCAL_TESTING_GUIDE.md for testing approach

## Contact & Support

For questions during development:
- Technical questions: Reference source code comments
- Architecture questions: See PLAN.md and BackgroundInfo.txt
- LSP integration: Voltage API documentation (separate)
- General questions: Contact project owner

---

**Good luck with the port! This is an important project for Bitcoin self-custody. 🚀⚡**
