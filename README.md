# Sport Results App

An Android application for following football leagues, fixtures and results. The app provides league standings, match schedules, detailed match information and personalized match tracking.

## Features

- **League Standings** – View league tables, including team positions, played matches and points.
- **Match Schedule** – Browse upcoming fixtures and search for matches by team name.
- **Match Results** – View completed matches and their scores.
- **Favorites** – Save matches, add personal notes and manage favorite matches.
- **Match Details** – View detailed match information, including team logos, scores, match status, venue and referee.
- **Notifications** – Receive reminders one hour before your favorite matches start.

## Tech Stack

- Kotlin
- Jetpack Compose
- Material Design 3
- Room Database
- Retrofit & Gson
- DataStore Preferences
- Jetpack Navigation Compose
- Coil

## API

Match data and league standings are provided by [football-data.org](https://www.football-data.org/) through its REST API.

The application supports different football competitions, configurable directly in the project.

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/Martin0945/sportResultsApp.git
   ```
2. Open the project in Android Studio.
3. Register for a free API key at [football-data.org](https://www.football-data.org/).
4. Configure your own API token in the local project configuration. See the API configuration instructions below.
5. Sync the Gradle project.
6. Run the application on an Android emulator or physical device.

### API Configuration

An API token is required to retrieve football data.

Each developer must provide their own token. Do not commit API tokens or other credentials to the repository.

Store your token locally in `local.properties`, which should not be committed to Git.

## Requirements

- Android 7.0 (API 24) or newer
- Android Studio
- A football-data.org API key

## Architecture

The application separates UI, data and business logic using Jetpack Compose screens, ViewModels and repositories. Room provides local data persistence, while Retrofit handles communication with the external API.
