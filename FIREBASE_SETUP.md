# Firebase Setup Guide

## Prerequisites
1. Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
2. Enable Authentication and Firestore in your Firebase project

## Setup Steps

### 1. Add Firebase to Your Project
1. Go to Project Settings in Firebase Console
2. Add Android app with package name: `com.readingjournal.app`
3. Download `google-services.json` and place it in `app/` directory
4. The file is already included in this project (demo version)

### 2. Enable Authentication
1. In Firebase Console, go to Authentication > Sign-in method
2. Enable Email/Password authentication
3. Optionally enable Google Sign-In for enhanced user experience

### 3. Enable Firestore Database
1. In Firebase Console, go to Firestore Database
2. Create database in production mode
3. Set up security rules (see below)

### 4. Security Rules
Add these rules to your Firestore database:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users can only access their own data
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Books are public for reading, but only authenticated users can write
    match /books/{bookId} {
      allow read: if true;
      allow write: if request.auth != null;
    }
    
    // Journal entries are private to each user
    match /journal_entries/{entryId} {
      allow read, write: if request.auth != null && 
        resource.data.userId == request.auth.uid;
    }
  }
}
```

### 5. API Keys
For Google Books API integration:
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Enable Books API
3. Create API key
4. Add the key to your app's configuration

## Features Enabled

### Authentication
- Email/Password sign up and sign in
- Password reset functionality
- User session management
- Secure user data access

### Database
- User-specific data storage
- Book information caching
- Journal entries with user isolation
- Real-time data synchronization

### Security
- User authentication required for data access
- Data isolation between users
- Secure API key management
- Protected user information

## Testing
1. Run the app on a device or emulator
2. Create a new account or sign in
3. Test book scanning and journaling features
4. Verify data persistence across app restarts

## Production Considerations
1. Replace demo `google-services.json` with your actual Firebase project file
2. Configure proper security rules for your use case
3. Set up proper API key restrictions
4. Enable app verification for additional security
5. Consider implementing user data backup/export features
