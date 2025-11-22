# Reading Journal - Android App

A modern Android reading app with intuitive navigation, emphasis on personal journaling and progress tracking.

## Features

### Core Features
- **Firebase Authentication**: Secure user login/signup with email and password
- **Barcode Scanner**: Scan book ISBNs to quickly add books to your library
- **Google Books API Integration**: Fetch book details automatically
- **Personal Journaling**: Create, edit, and organize reading journal entries
- **Reading Progress Tracking**: Track books, pages read, and reading statistics
- **AI-Powered Suggestions**: Get personalized book recommendations and reading insights
- **Modern UI**: Beautiful Material 3 design with Jetpack Compose
- **RecyclerView Optimization**: Efficient book list display with smooth scrolling
- **LiveData Integration**: Reactive UI updates with proper lifecycle management

### Planned Features
- **Social Sharing**: Share reading progress and favorite quotes
- **Book Club Integration**: Join book clubs and participate in discussions
- **Advanced AI Features**: Reading habit analysis and personalized recommendations

## Architecture

The app follows modern Android development best practices:

- **MVVM Architecture**: Clean separation of concerns with ViewModels
- **Jetpack Compose**: Modern declarative UI framework
- **Room Database**: Local data persistence with SQLite
- **Hilt Dependency Injection**: Clean dependency management
- **Navigation Compose**: Type-safe navigation
- **Material 3 Design**: Latest Material Design guidelines

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM + Repository Pattern
- **Database**: Room (SQLite) + Firebase Firestore
- **Authentication**: Firebase Authentication
- **Dependency Injection**: Hilt
- **Navigation**: Navigation Compose
- **Async Operations**: Kotlin Coroutines & Flow + LiveData
- **Image Loading**: Coil
- **Networking**: Retrofit + Google Books API
- **Barcode Scanning**: ZXing
- **UI Components**: Material 3 Design System

## Project Structure

```
app/src/main/java/com/readingjournal/app/
├── data/
│   ├── database/          # Room database setup
│   ├── model/            # Data models (Book, JournalEntry, etc.)
│   └── repository/       # Repository pattern implementation
├── di/                   # Dependency injection modules
├── ui/
│   ├── components/       # Reusable UI components
│   ├── navigation/       # Navigation setup
│   ├── screens/          # Screen composables and ViewModels
│   └── theme/           # Material 3 theme configuration
└── MainActivity.kt      # Main activity
```

## Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 8 or later
- Android SDK 24 or later

### Setup
1. Clone the repository
2. Open the project in Android Studio
3. Set up Firebase (see [FIREBASE_SETUP.md](FIREBASE_SETUP.md) for detailed instructions)
4. Replace the demo `google-services.json` with your Firebase project file
5. Sync the project with Gradle files
6. Run the app on an emulator or physical device

### Firebase Setup
The app requires Firebase for authentication and data storage. Follow the detailed setup guide in [FIREBASE_SETUP.md](FIREBASE_SETUP.md) to:
- Create a Firebase project
- Enable Authentication and Firestore
- Configure security rules
- Set up Google Books API access

### Build Configuration
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 34

## Key Screens

### Authentication Screen
- Email/password sign up and sign in
- Password reset functionality
- Secure user authentication
- Firebase integration

### Home Screen
- Welcome message with personalized greeting
- Quick stats overview (books read, pages read, reading streak)
- Currently reading books carousel
- AI-powered suggestions

### Scan/Add Books Screen
- Barcode scanner for ISBN scanning
- Google Books API integration
- Manual book search and addition
- Book details preview

### Journal Screen
- List of all journal entries
- Create new journal entries
- Favorite entries functionality
- Entry tags and search

### Progress Screen
- Reading statistics and analytics
- Monthly progress tracking
- Reading goals management
- Visual progress charts (planned)

### Profile Screen
- User profile information
- App settings and preferences
- Data export and privacy controls
- Social features configuration

## Data Models

### Book
- Title, author, total pages, current page
- Rating, notes, completion status
- Cover image, ISBN, genre
- Date added and completed

### Journal Entry
- Title, content, creation/modification dates
- Associated book reference
- Mood, tags, favorite status

### Reading Session
- Start/end time, duration
- Pages read, associated book
- Reading analytics data

### AI Suggestion
- Suggestion type (book recommendation, goal, habit)
- Title, description, priority
- Read status and metadata

## Future Enhancements

1. **Advanced AI Integration**
   - Reading behavior analysis
   - Personalized book recommendations
   - Reading habit suggestions

2. **Social Features**
   - Friend connections
   - Reading challenges
   - Book club participation
   - Quote sharing

3. **Enhanced Analytics**
   - Reading speed tracking
   - Genre preferences analysis
   - Reading goal achievements
   - Progress visualizations

4. **Offline Support**
   - Full offline functionality
   - Data synchronization
   - Backup and restore
