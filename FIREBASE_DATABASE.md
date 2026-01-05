Place your Firebase Realtime Database JSON export here and rename to `firebase-database.json` if you want the app to (manually) read it.

Suggested path in this project:
- `src/main/resources/firebase-database.json` (not checked into repo if it contains secrets)

Sample structure (already included in `src/main/resources/firebase-database.json.sample`):
- `books`: map of book objects (id, title, author, issued, issuedTo)
- `students`: map of student objects (id, name)
- `history`: map of action entries (type, text, ts)

Notes:
- This project currently stores data in-memory; adding server-side loading from a JSON file or from Firebase requires additional code.
- If you want me to implement automatic loading from this JSON file into the application's `LibraryService` on startup, say so and I will add it.
