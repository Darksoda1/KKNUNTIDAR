# KKNTrack (KKN-UNTIDAR)

An Android application for tracking KKN activities (community service program) developed for Universitas Tidar (UNTIDAR). The app provides user registration/login, activity logging (with optional photo), location support, announcements, and basic profile management. The project contains an Android client (`kknuntidar-app`) and a server module placeholder (`kknuntidar-server`).

## Key features
- User registration and login
- Local SQLite storage for users and activities
- Create, edit, delete activity entries (title, description, date, time, location, optional photo)
- Dashboard with total activities and last activity
- Announcements screen
- Location screen (uses device location APIs)
- Profile management, change password, edit profile
- Basic app settings and logout flow

## Tech stack & notable libraries
- Android (Java)
- Min SDK: 27, Target/Compile SDK: 35
- SQLite (via custom `DatabaseHelper`)
- Retrofit + OkHttp (networking)
- Gson converter
- Kotlin Coroutines (present in Gradle deps — used for async tasks)
- AndroidX libraries: core-ktx, appcompat, material, lifecycle (ViewModel / LiveData), constraintlayout

Dependencies (from `kknuntidar-app/build.gradle.kts`):
- Retrofit 2.9.0, OkHttp 4.11.0
- kotlinx-coroutines-android / core 1.7.3
- androidx.lifecycle:viewmodel-ktx & livedata-ktx

## Permissions
The app requests the following runtime permissions (declared in `AndroidManifest.xml`):
- INTERNET
- ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION
- CAMERA
- READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE

## Project structure (important files)
- `kknuntidar-app/` — Android application module
  - `src/main/AndroidManifest.xml` — manifest and activity registrations
  - `src/main/java/com/untidar/kkntrack/` — app Java sources
    - `MainActivity.java` — main dashboard and navigation
    - `SplashActivity.java`, `LoginActivity.java`, `RegisterActivity.java`, `ProfileActivity.java`, `ActivityLogActivity.java`, `ReportActivity.java`, `AnnouncementActivity.java`, `LocationActivity.java`, `EditProfileActivity.java`, `ChangePasswordActivity.java`, `ActivityDetailActivity.java`, `SettingsActivity.java`
    - `database/DatabaseHelper.java` — SQLite helper for `users` and `activities` tables
    - `adapter/` — adapters for RecyclerViews (activities / announcements)
    - `model/` — model classes: `User`, `Activity`, `Announcement` (domain objects)
  - `res/` — layouts, drawables, menus and resources
- `kknuntidar-server/` — server module (Gradle module present, details not examined)

## How to build (Windows / PowerShell)
1. Open the project in Android Studio (recommended) and let Gradle sync.
2. To build a debug APK from the repo root using the included Gradle wrapper (PowerShell):

```powershell
.\gradlew assembleDebug
```

3. To install to a connected device/emulator:

```powershell
.\gradlew installDebug
```

Or use Android Studio Run/Debug targets.

## Notes & assumptions
- The Android module uses Java for sources but includes Kotlin-related Gradle config and coroutine dependencies. Kotlin may be used in other parts or planned features.
- The app stores users and activities locally in SQLite; there are Retrofit dependencies for network calls if an API server is provided or planned (check `kknuntidar-server` module).
- `DatabaseHelper` schema includes a `photo_path` column (DB version 2) — app stores photo file paths rather than blobs.

## Running and testing
- Use Android Studio's emulator or a real device with USB debugging enabled.
- Ensure runtime permissions are granted for camera, storage, and location features.

## Contributing
1. Fork the repository.
2. Create a branch: `feature/your-feature`.
3. Make changes and test on emulator/device.
4. Open a pull request describing changes.

Before contributing, please add or communicate a coding convention / PR checklist if desired.

## License
This project is provided under the MIT License. See the `LICENSE` file in the repository root for the full license text and copyright information.

## Maintainers / Contact
Maintainer information and contact guidance are available in `MAINTAINERS.md` in the repository root. The `MAINTAINERS.md` file currently contains placeholder contact info and instructions for replacing it with the real maintainer names/emails.

Files added in this update
- `LICENSE` — MIT license added to the project root (copyright 2025 Universitas Tidar by default).
- `MAINTAINERS.md` — maintainer contacts, roles, and contribution guidance (contains placeholders; please update with real contact details).

---
Generated README summary based on: `kknuntidar-app/build.gradle.kts`, `kknuntidar-app/src/main/AndroidManifest.xml`, `kknuntidar-app/src/main/java/com/untidar/kkntrack/MainActivity.java`, and `kknuntidar-app/src/main/java/com/untidar/kkntrack/database/DatabaseHelper.java`.
