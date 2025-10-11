# PLAN.md — Migrate Voski to JavaScript/TypeScript Cross-Platform Architecture

## Goal
Transform the existing Kotlin/Android Voski codebase into a JavaScript/TypeScript cross-platform solution that can deploy to both iOS and Android from a single codebase, while maintaining all Lightning wallet functionality.

## Success Criteria
- [ ] Choose optimal JS/TS cross-platform framework (React Native + Expo recommended)
- [ ] Create new project structure with shared TypeScript business logic
- [ ] Port core Lightning wallet functionality from Kotlin to TypeScript
- [ ] Implement platform-specific native bridges for cryptographic operations
- [ ] Maintain existing Voltage LSP integration and voucher system
- [ ] Achieve feature parity with current Android implementation
- [ ] Successfully build and deploy to both iOS and Android
- [ ] Performance testing confirms acceptable UX for wallet operations

## Scope & Architecture Decision

**RECOMMENDED FRAMEWORK: React Native with Expo**

Based on comprehensive research, React Native with Expo is the optimal choice for Bitcoin wallet development because:

1. **Proven track record**: BlueWallet (major Bitcoin wallet) built with React Native
2. **Mature crypto ecosystem**: Extensive libraries and resources for cryptocurrency apps
3. **Performance**: Native bridge for crypto operations, acceptable for wallet UX
4. **Security**: Proven security patterns for private key management
5. **OTA Updates**: Expo allows instant app updates without app store approval
6. **Developer experience**: Strong TypeScript support, hot reload, extensive tooling
7. **Platform coverage**: iOS, Android, and Web from single codebase

## New Project Structure

```
voski-app/
├── src/
│   ├── components/           # React Native UI components
│   │   ├── auth/            # PIN, biometric authentication
│   │   ├── payments/        # Send/receive payment screens
│   │   ├── settings/        # Wallet configuration
│   │   └── common/          # Shared UI components
│   ├── business/            # Core business logic (TypeScript)
│   │   ├── wallet/          # Wallet management and crypto operations
│   │   ├── lightning/       # Lightning network logic
│   │   ├── lsp/            # LSP client (port from Kotlin)
│   │   ├── backup/         # Encrypted backup system
│   │   └── voucher/        # Voucher redemption logic
│   ├── services/           # Platform services and native bridges
│   │   ├── crypto/         # Native crypto bridge
│   │   ├── storage/        # Secure storage
│   │   └── notifications/  # Push notification handling
│   ├── navigation/         # Screen navigation
│   ├── hooks/             # React hooks for state management
│   └── utils/             # Utility functions
├── ios/                   # iOS-specific code and configuration
├── android/              # Android-specific code and configuration
├── app.json              # Expo configuration
├── package.json          # Dependencies
└── tsconfig.json         # TypeScript configuration
```

## File Map (create/edit/remove from main repo)

### Phase 1: Project Setup & Dependencies
**CREATE:**
- `voski-app/package.json` - React Native + Expo dependencies
- `voski-app/app.json` - Expo configuration with crypto wallet settings
- `voski-app/tsconfig.json` - TypeScript configuration
- `voski-app/babel.config.js` - Babel configuration for React Native
- `voski-app/metro.config.js` - Metro bundler configuration

### Phase 2: Core TypeScript Business Logic
**CREATE:**
- `voski-app/src/business/wallet/WalletManager.ts` - Port from WalletManager.kt
- `voski-app/src/business/lightning/LightningClient.ts` - Lightning network operations
- `voski-app/src/business/lsp/VoltageLspClient.ts` - Port from VoltageLspClient.kt
- `voski-app/src/business/backup/BackupService.ts` - Encrypted backup functionality
- `voski-app/src/business/voucher/VoucherRedeemer.ts` - Voucher system
- `voski-app/src/business/config/VoskiConfig.ts` - Central configuration

### Phase 3: Native Bridges for Crypto Operations
**CREATE:**
- `voski-app/src/services/crypto/CryptoNativeBridge.ts` - Interface for native crypto
- `voski-app/ios/VoskiCrypto/` - iOS native crypto module (Swift)
- `voski-app/android/app/src/main/java/crypto/` - Android native crypto module (Java/Kotlin)

### Phase 4: React Native UI Components
**CREATE:**
- `voski-app/src/components/auth/PinInput.tsx` - PIN authentication UI
- `voski-app/src/components/payments/SendScreen.tsx` - Send payment screen
- `voski-app/src/components/payments/ReceiveScreen.tsx` - Receive payment screen
- `voski-app/src/components/payments/PaymentHistory.tsx` - Payment history
- `voski-app/src/components/settings/SettingsScreen.tsx` - Wallet settings
- `voski-app/src/components/common/AmountInput.tsx` - Amount input component

### Phase 5: Navigation & State Management
**CREATE:**
- `voski-app/src/navigation/RootNavigator.tsx` - Main app navigation
- `voski-app/src/hooks/useWallet.ts` - Wallet state management hook
- `voski-app/src/hooks/useLightning.ts` - Lightning operations hook
- `voski-app/src/contexts/WalletContext.tsx` - React context for wallet state

### Phase 6: Platform Services
**CREATE:**
- `voski-app/src/services/storage/SecureStorage.ts` - Cross-platform secure storage
- `voski-app/src/services/notifications/NotificationService.ts` - Push notifications
- `voski-app/src/services/background/BackgroundSync.ts` - Background sync

### Phase 7: Testing & CI/CD
**CREATE:**
- `voski-app/__tests__/` - Jest test files
- `voski-app/.github/workflows/expo-build.yml` - Expo CI/CD pipeline
- `voski-app/e2e/` - End-to-end tests with Detox

## Migration Strategy

### Phase 1: Environment Setup (Day 1)
1. Initialize new React Native + Expo project
2. Set up TypeScript configuration
3. Configure development environment
4. Set up basic navigation structure

### Phase 2: Core Business Logic Port (Days 2-5)
1. **Wallet Management**: Port WalletManager from Kotlin to TypeScript
2. **Lightning Operations**: Port Lightning network functionality
3. **LSP Client**: Port VoltageLspClient.kt to TypeScript
4. **Configuration**: Port VoskiConfig and environment setup

### Phase 3: Native Bridge Development (Days 6-8)
1. **Crypto Operations**: Create native modules for secp256k1, key generation
2. **Secure Storage**: Platform-specific secure key storage
3. **Biometric Authentication**: Native biometric integration

### Phase 4: UI Implementation (Days 9-12)
1. **Authentication Flow**: PIN, biometric screens
2. **Payment Screens**: Send/receive with QR scanning
3. **Wallet Management**: Balance, transaction history
4. **Settings**: Configuration and backup options

### Phase 5: Integration & Testing (Days 13-15)
1. **End-to-end Integration**: Connect UI to business logic
2. **Platform Testing**: Test on both iOS and Android
3. **Performance Optimization**: Optimize for mobile UX
4. **Security Audit**: Review crypto operations and key storage

## Key Dependencies

```json
{
  "dependencies": {
    "@react-native-community/netinfo": "^11.3.1",
    "@react-native-async-storage/async-storage": "^1.23.1",
    "@react-native-camera-roll/camera-roll": "^7.4.0",
    "expo": "~50.0.0",
    "expo-camera": "~14.1.0",
    "expo-clipboard": "~5.0.0",
    "expo-crypto": "~12.8.0",
    "expo-local-authentication": "~13.8.0",
    "expo-secure-store": "~12.9.0",
    "react": "18.2.0",
    "react-native": "0.73.6",
    "react-native-keychain": "^8.2.0",
    "react-native-qrcode-svg": "^6.3.0",
    "react-native-vision-camera": "^3.9.0",
    "react-navigation": "^6.1.0"
  },
  "devDependencies": {
    "@types/react": "~18.2.79",
    "@types/react-native": "~0.73.0",
    "typescript": "~5.3.0",
    "jest": "^29.7.0",
    "detox": "^20.18.0"
  }
}
```

## Native Crypto Libraries Required

### Bitcoin/Lightning Crypto:
- **secp256k1**: Native elliptic curve operations
- **bech32**: Bitcoin address encoding
- **lightning-bolt11**: Lightning invoice parsing
- **scrypt**: Key derivation for backups

### React Native Crypto Bridges:
- `react-native-crypto-js`: Cryptographic utilities
- `react-native-bip39`: Mnemonic seed handling
- `react-native-randombytes`: Secure random number generation

## Test Plan

**Commands to run:**
```bash
# Development
npm start                    # Start Expo development server
npm run ios                  # Run on iOS simulator
npm run android             # Run on Android emulator

# Testing
npm test                    # Run Jest unit tests
npm run test:e2e:ios       # Run E2E tests on iOS
npm run test:e2e:android   # Run E2E tests on Android

# Building
expo build:ios             # Build iOS app
expo build:android         # Build Android APK/AAB
```

**What we expect to see:**
- React Native app launches on both iOS and Android
- Wallet initialization with mnemonic generation works
- Lightning invoice generation and payment processing
- Secure key storage and biometric authentication
- Voltage LSP integration maintains functionality
- Push notifications work for offline payments
- Encrypted backup system operational
- Performance acceptable for wallet operations (< 2s for typical operations)

## Risks & Mitigations

**Risk:** Performance degradation vs native Kotlin implementation
**Mitigation:** Use native bridges for crypto operations, optimize critical paths, performance testing

**Risk:** Crypto library compatibility across platforms
**Mitigation:** Thoroughly test crypto operations, use proven React Native crypto libraries

**Risk:** Platform-specific native code complexity
**Mitigation:** Leverage Expo managed workflow, minimize custom native code

**Risk:** Loss of existing functionality during migration
**Mitigation:** Comprehensive feature mapping, phased migration with testing at each step

**Risk:** Development timeline expansion
**Mitigation:** Start with MVP feature set, incremental enhancement approach

## Open Questions

1. **Crypto Library Strategy**: Should we use JavaScript crypto libraries or native bridges for all operations?
2. **State Management**: Redux vs Zustand vs React Context for wallet state?
3. **Offline Capability**: How to handle Lightning operations when network is unavailable?
4. **App Store Compliance**: Any specific requirements for crypto wallets on iOS/Android stores?
5. **Performance Benchmarks**: What are acceptable performance targets for wallet operations?
6. **Migration Timeline**: Should this be a complete rewrite or gradual migration?

## Next Steps

1. **Proof of Concept**: Create minimal React Native app with basic wallet functionality
2. **Performance Testing**: Benchmark crypto operations vs current Kotlin implementation
3. **Architecture Validation**: Validate chosen patterns with core wallet operations
4. **Timeline Refinement**: Based on PoC results, refine migration timeline and approach