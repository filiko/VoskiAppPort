# Voski App - Modern Kotlin Android Application

A modern, feature-rich digital wallet application built with Kotlin and Jetpack Compose.

## 📱 Project Structure

```
newStuctureFORVoskiApp/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/voskiapp/
│   │       │   ├── MainActivity.kt
│   │       │   ├── ui/
│   │       │   │   ├── screens/
│   │       │   │   │   ├── auth/
│   │       │   │   │   │   └── RememberMeScreen.kt
│   │       │   │   │   ├── wallet/
│   │       │   │   │   │   ├── WalletScreen.kt
│   │       │   │   │   │   └── WalletViewModel.kt
│   │       │   │   │   ├── card/
│   │       │   │   │   │   ├── CardScreen.kt
│   │       │   │   │   │   └── CardViewModel.kt
│   │       │   │   │   ├── casestudy/
│   │       │   │   │   │   └── CaseStudyScreen.kt
│   │       │   │   │   ├── create/
│   │       │   │   │   │   ├── CreateScreen.kt
│   │       │   │   │   │   └── CreateViewModel.kt
│   │       │   │   │   ├── details/
│   │       │   │   │   │   ├── DetailsScreen.kt
│   │       │   │   │   │   └── DetailsViewModel.kt
│   │       │   │   │   ├── online/
│   │       │   │   │   │   └── OnlineScreen.kt
│   │       │   │   │   └── getstarted/
│   │       │   │   │       └── GetStartedScreen.kt
│   │       │   │   ├── components/
│   │       │   │   │   ├── ReusableButton.kt
│   │       │   │   │   ├── ReusableCard.kt
│   │       │   │   │   ├── ReusableTextField.kt
│   │       │   │   │   └── ReusableDialog.kt
│   │       │   │   └── theme/
│   │       │   │       ├── Color.kt
│   │       │   │       ├── Theme.kt
│   │       │   │       └── Type.kt
│   │       │   ├── data/
│   │       │   │   ├── models/
│   │       │   │   │   ├── User.kt
│   │       │   │   │   ├── Wallet.kt
│   │       │   │   │   └── Card.kt
│   │       │   │   └── repository/
│   │       │   │       ├── WalletRepository.kt
│   │       │   │       └── CardRepository.kt
│   │       │   ├── domain/
│   │       │   │   └── usecases/
│   │       │   │       └── GetWalletBalanceUseCase.kt
│   │       │   └── utils/
│   │       │       └── BuilderPattern.kt
│   │       ├── res/
│   │       │   ├── values/
│   │       │   │   ├── strings.xml
│   │       │   │   └── themes.xml
│   │       │   └── xml/
│   │       │       ├── backup_rules.xml
│   │       │       └── data_extraction_rules.xml
│   │       └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── .gitignore
└── README.md
```

## 🏗️ Architecture

This project follows **Clean Architecture** principles with:

- **Presentation Layer**: Jetpack Compose UI with ViewModels
- **Domain Layer**: Use cases for business logic
- **Data Layer**: Repositories and data models

### Key Design Patterns

1. **MVVM (Model-View-ViewModel)**: For UI architecture
2. **Repository Pattern**: For data access abstraction
3. **Builder Pattern**: For complex object creation
4. **Use Cases**: For encapsulating business logic

## 🎨 Features

### Screens

1. **Get Started Screen**: Welcome/onboarding screen
2. **Remember Me Screen**: Authentication with remember me functionality
3. **Wallet Screen**: View balance and recent transactions
4. **Card Screen**: Manage payment cards with beautiful gradient designs
5. **Create Screen**: Add new payment cards
6. **Details Screen**: View transaction details
7. **Online Screen**: Access online services and features
8. **Case Study Screen**: Success stories and testimonials

### Reusable Components

- **ReusableButton**: Customizable button component
- **ReusableCard**: Card component with multiple variants
- **ReusableTextField**: Text input with validation support
- **ReusableDialog**: Dialog components for user interactions

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: Clean Architecture + MVVM
- **Navigation**: Jetpack Navigation Compose
- **Async**: Kotlin Coroutines + Flow
- **DI**: (Ready for Hilt/Koin integration)

## 📦 Dependencies

- Jetpack Compose (UI)
- Material 3 (Design)
- Navigation Compose (Navigation)
- Lifecycle ViewModel (State management)
- Kotlin Coroutines (Async operations)

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- Minimum SDK 24
- Target SDK 34
- JDK 8 or later

### Building the Project

1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Run the app on an emulator or device

```bash
./gradlew build
```

### Running the App

```bash
./gradlew installDebug
```

## 📱 Screens Flow

```
GetStarted → RememberMe → Wallet → Card → Create
                                    ↓
                                 Details → Online
```

## 🎯 Key Features

- **Modern UI**: Built with Jetpack Compose and Material 3
- **Responsive Design**: Adapts to different screen sizes
- **Type-Safe Navigation**: Using Navigation Compose
- **State Management**: ViewModels with StateFlow
- **Reusable Components**: DRY principle with custom composables
- **Clean Code**: Following SOLID principles

## 📝 Code Style

This project follows the official Kotlin coding conventions:
- Package names in lowercase
- Class names in PascalCase
- Function names in camelCase
- Meaningful variable names
- Proper indentation and formatting

## 🔐 Security Considerations

- Sensitive data (CVV, full card numbers) should never be stored locally
- Use Android Keystore for storing sensitive information
- Implement proper SSL pinning for network calls
- Add ProGuard rules for release builds

## 🧪 Testing

(Ready for implementation)
- Unit tests with JUnit
- UI tests with Compose Test
- Integration tests

## 📄 License

This project is part of Voski App development.

## 👥 Contributing

Follow the existing code structure and patterns when adding new features.

## 📞 Support

For questions and support, contact the development team.

---

Built with ❤️ using Kotlin and Jetpack Compose



