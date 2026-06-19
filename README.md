# FullStackCourseApp

FullStackCourseApp is an offline-capable Android application designed to teach full-stack web development through structured lessons, interactive quizzes, and hands-on coding challenges.

## 🚀 Features

- **📚 Structured Lessons**: Comprehensive coverage of HTML, CSS, JavaScript, React, Node.js, Databases, and DevOps.
- **📝 Interactive Quizzes**: Test your knowledge with multiple-choice questions and get instant feedback with explanations.
- **💻 Coding Challenges**: Hands-on practice with starter code, hints, and sample solutions to solidify your understanding.
- **📊 Progress Tracking**: Earn XP as you complete lessons, quizzes, and challenges. Monitor your journey in the detailed progress dashboard.
- **🌙 Developer Dark Theme**: A sleek, modern UI designed for long learning sessions.
- **📴 Offline First**: All content is bundled with the app, allowing you to learn anytime, anywhere, without an internet connection.

## 🛠️ Technologies Used

- **Language**: Kotlin
- **UI Framework**: Android Views with ViewBinding
- **Navigation**: Jetpack Navigation Component
- **Markdown Rendering**: [Markwon](https://github.com/noties/Markwon)
- **Data Serialization**: Gson
- **Local Storage**: SharedPreferences
- **Theme**: Material Components (Dark Theme)

## 📂 Project Structure

```
app/
├── src/main/
│   ├── assets/lessons/       # Markdown lesson files
│   ├── java/com/tanfullstack/courseapp/
│   │   ├── data/
│   │   │   ├── models/       # Data classes (Module, Lesson, Quiz, etc.)
│   │   │   └── repository/   # Static data and Progress tracking
│   │   ├── ui/
│   │   │   ├── home/         # Home screen and Module adapter
│   │   │   ├── lesson/       # Module and Lesson details
│   │   │   ├── quiz/         # Quiz interaction logic
│   │   │   ├── challenge/    # Coding challenge logic
│   │   │   └── progress/     # Progress dashboard
│   │   ├── utils/            # Utility classes (MarkdownLoader)
│   │   └── MainActivity.kt   # Entry point with Bottom Navigation
│   └── res/
│       ├── layout/           # XML Layout definitions
│       ├── navigation/       # Navigation graph
│       ├── menu/             # Bottom navigation menu
│       └── values/           # Colors, Strings, and Themes
```

## 🏁 Getting Started

1.  **Clone the Repository**:
    ```bash
    git clone https://github.com/your-username/FullStackCourseApp.git
    ```
2.  **Open in Android Studio**:
    Open the project folder in Android Studio (Ladybug or newer).
3.  **Sync Gradle**:
    Let the project sync and download dependencies.
4.  **Run the App**:
    Connect an Android device or start an emulator (API 26+) and click the **Run** button.

## 📝 License

This project is open-source and available under the [MIT License](LICENSE).
